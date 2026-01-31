# Dockerfile.oracle
FROM container-registry.oracle.com/database/express:21.3.0-xe

VOLUME ["/opt/oracle/oradata"]

EXPOSE 1521 5500

RUN chown -R oracle:oracle /opt/oracle/oradata

USER oracle

CMD ["sh", "-c", "exec /opt/oracle/runOracle.sh"]
