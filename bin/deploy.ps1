$PROJECT_ROOT = "$PSScriptRoot\.."

#build backend
Set-Location "$PROJECT_ROOT"
mvn clean package

#build frontend
npm run build --prefix "$PROJECT_ROOT\front-UI"

#deploy backend
$WAR = Get-ChildItem -Path "$PROJECT_ROOT\target" *.war | Select-Object -First 1
asadmin --host 10.31.201.3 --port 4848 --user admin --passwordfile "$PROJECT_ROOT\config\payara-password" redeploy --name "ssbd01" "$PROJECT_ROOT\target\$WAR"

#deploy frontend
cd C:\
cd '.\Program Files (x86)\WinSCP\'
./winscp.exe scp://student@10.31.201.2:22/var/www/html /synchronize "$PROJECT_ROOT\front-UI\build" /var/www/html

