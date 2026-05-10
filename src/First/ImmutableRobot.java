package First;

public final class ImmutableRobot {
    private final String name;
    private final MutablePoint position;

    public ImmutableRobot(String name, MutablePoint position) {
        this.name = name;
        this.position = new MutablePoint(position);
    }

    public String getName() {
        return name;
    }

    public MutablePoint getPosition() {
        return new MutablePoint(position);
    }
}
