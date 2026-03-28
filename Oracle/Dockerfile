# Dockerfile.oracle
FROM container-registry.oracle.com/database/express:21.3.0-xe

VOLUME ["/opt/oracle/oradata"]

EXPOSE 1521 5500

CMD ["sh", "-c", "exec /opt/oracle/runOracle.sh"]