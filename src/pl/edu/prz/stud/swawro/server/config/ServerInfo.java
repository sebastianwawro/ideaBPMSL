package pl.edu.prz.stud.swawro.server.config;

public class ServerInfo {
    private String ip;
    private String kryoPort;
    private String httpPort;
    private String internalPort;
    private int maxConnections;
    private int connections;
    private int imporance;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getKryoPort() {
        return kryoPort;
    }

    public void setKryoPort(String kryoPort) {
        this.kryoPort = kryoPort;
    }

    public String getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(String httpPort) {
        this.httpPort = httpPort;
    }

    public String getInternalPort() {
        return internalPort;
    }

    public void setInternalPort(String internalPort) {
        this.internalPort = internalPort;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getConnections() {
        return connections;
    }

    public void setConnections(int connections) {
        this.connections = connections;
    }

    public int getImporance() {
        return imporance;
    }

    public void setImporance(int imporance) {
        this.imporance = imporance;
    }
}
