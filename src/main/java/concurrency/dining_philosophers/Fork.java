package concurrency.dining_philosophers;

class Fork {

    private final int index;

    Fork(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "Fork " + index;
    }
}
