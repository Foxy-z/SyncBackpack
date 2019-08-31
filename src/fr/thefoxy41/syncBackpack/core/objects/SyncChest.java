package fr.thefoxy41.syncBackpack.core.objects;

public class SyncChest {
    private String name;
    private SimpleLocation location;

    public SyncChest(String name, SimpleLocation location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SimpleLocation getLocation() {
        return this.location;
    }

    public void setLocation(SimpleLocation location) {
        this.location = location;
    }
}
