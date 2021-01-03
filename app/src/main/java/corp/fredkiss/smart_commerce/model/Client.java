package corp.fredkiss.smart_commerce.model;

public abstract class Client {
    private String nomClient;
    private String chbreClient;

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getChbreClient() {
        return chbreClient;
    }

    public void setChbreClient(String chbreClient) {
        this.chbreClient = chbreClient;
    }

    public Client(String nomClient, String chbreClient) {
        this.nomClient = nomClient;
        this.chbreClient = chbreClient;
    }
}
