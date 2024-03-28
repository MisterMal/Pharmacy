package pl.lodz.p.it.ssbd2023.ssbd01.security;

import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;

@DatabaseIdentityStoreDefinition(
    dataSourceLookup = "java:app/jdbc/ssbd01auth",
    callerQuery = "select distinct password from public.glassfish_auth_view where login = ?",
    groupsQuery = "select access_level_role from public.glassfish_auth_view where login = ?",
    hashAlgorithm = HashAlgorithmImpl.class)
public class PostgresIdentityStore {}
