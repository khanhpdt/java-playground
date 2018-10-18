package fundamentals;

// If there is no public class, the file name can be any valid name.
// If there is, the name must be the name of that public class.
class FileWithMultiClasses {

    public static void main(String[] args) {
        System.out.println("This is a file containing multiple classes.");
        new AnotherClass();
    }

}

class AnotherClass {
    AnotherClass() {
        System.out.println("This is another class in the file.");
    }
}

class AnotherClass1 {
    AnotherClass1() {
        System.out.println("This is another another class in the file");
    }
}