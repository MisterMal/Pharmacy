package controller;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import static org.hibernate.sql.ast.SqlTreeCreationLogger.LOGGER;

public abstract class BaseTest {

  static final Network NETWORK = Network.newNetwork();

  static final int TEST_CLASS_COUNT = 5;

  static int testClassesRan = 0;

  static void afterAll() {
    testClassesRan++;
    if(testClassesRan == TEST_CLASS_COUNT) {
      postgreSQLContainer.close();
      payaraServerContainer.close();
    }
  }

  static MountableFile warFile =
      MountableFile.forHostPath(Paths.get("target/ssbd01-0.0.1.war").toAbsolutePath());

  static String getApiRoot() {
//    return "http://localhost:8080/api";
    return String.format(
        "http://%s:%s/api", payaraServerContainer.getHost(), payaraServerContainer.getMappedPort(8080));
  }

  static final PostgreSQLContainer postgreSQLContainer;

  static final GenericContainer payaraServerContainer;

  static {
    postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"))
                    .withNetwork(NETWORK)
                    .withDatabaseName("ssbd01")
                    .waitingFor(Wait.defaultWaitStrategy())
                    .withExposedPorts(5432)
                    .withInitScript("init_accounts.sql")
                    .withUsername("ssbd01admin")
                    .withPassword("admin")
                    .withNetworkAliases("ssbd_db")
                    .withReuse(true);
    postgreSQLContainer.start();

    payaraServerContainer =
            new GenericContainer<>("payara/server-full:6.2023.2-jdk17")
                    .withExposedPorts(8080)
//                    .withLogConsumer(new Slf4jLogConsumer(LOGGER).withPrefix("Service"))
                    .withLogConsumer(outputFrame -> System.out.println(outputFrame.getUtf8String()))
                    .dependsOn(postgreSQLContainer)
                    .withNetwork(NETWORK)
                    .withCopyToContainer(warFile, "/opt/payara/deployments/ssbd01-0.0.1.war")
                    .waitingFor(Wait.forHttp("/health/ready").forStatusCode(200))
                    .withStartupTimeout(java.time.Duration.ofSeconds(360))
                    .withReuse(true);
    payaraServerContainer.start();
  }


}
