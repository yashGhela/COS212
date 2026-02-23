public class Main {
        public static void main(String[] args) {
                /*
                 * Output in out.txt
                 */
                Gradebook g = new Gradebook(new String[] { "Prac1", "Prac2", "Prac3", "Prac4", "Prac5" });
                g.addStudent("u1", new int[] { 78, 64, 91, 55, 83 });
                g.addStudent("u2", new int[] { 42, 88, 73, 70, 57 });
                g.addStudent("u3", new int[] { 90, 76, 84, 92, 68 });
                g.addStudent("u4", new int[] { 61, 49, 70, 58, 81 });
                g.addStudent("u5", new int[] { 85, 93, 67, 74, 60 });
                System.out.println(g);
                System.out.println(g.assignmentStats("Prac3"));
                System.out.println(g.studentStats("u3"));
        }
}
