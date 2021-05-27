package fundamentals;

public class PrivateConstructor {

    public PrivateConstructor() {

    }

    private PrivateConstructor(int a) {
        System.out.println("PrivateConstructor: " + a);
    }

    class InnerClass extends PrivateConstructor {
        InnerClass() {
            super(1);
            System.out.println("I can call the private constructor b/c i'm an inner class");
        }
    }

}

class Child extends PrivateConstructor {
    public Child() {
        super();
        System.out.println("I can call non-private constructor");

        // super(1); // compile error
    }
}

class PrivateConstructor2 {
    private PrivateConstructor2() {
        // i only have one constructor and it's private
    }
}

//class Child2 extends PrivateConstructor2 {} // compile error
