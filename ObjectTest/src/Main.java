
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, world.");
        
        Student s = new Student("memai");
        System.out.println(s.getEmail());
        
        s.addCourse("1");
        s.addCourse("1");
        System.out.println(s.getCourseIds());
    }
}
