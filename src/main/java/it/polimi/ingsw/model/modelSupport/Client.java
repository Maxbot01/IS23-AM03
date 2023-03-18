package it.polimi.ingsw.model.modelSupport;

public class Client {
    private String IP;
    private ConnectionType withConnectionType;
    private SenderConnectionStrategy connectionStrategy;

    public Client(String IP, ConnectionType withConnectionType, SenderConnectionStrategy connectionStrategy) {
        this.IP = IP;
        this.withConnectionType = withConnectionType;
        this.connectionStrategy = connectionStrategy;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getIP() {
        return IP;
    }

    public void setConnectionType(ConnectionType withConnectionType) {
        this.withConnectionType = withConnectionType;
    }

    public ConnectionType getConnectionType() {
        return withConnectionType;
    }

    public void setConnectionStrategy(SenderConnectionStrategy connectionStrategy) {
        this.connectionStrategy = connectionStrategy;
    }

    public SenderConnectionStrategy getConnectionStrategy() {
        return connectionStrategy;
    }

    // metodo UML non chiaro
}
