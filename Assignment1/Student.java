/*
    This class is given to you and will be overwritten
*/
public class Student {
    public String studentNumber;
    public int[] marks;
    public Student[] next;
    public Student[] prev;

    public Student(String studentNumber, int[] marks) {
        this.studentNumber = studentNumber;
        this.marks = marks;
        next = new Student[marks.length];
        prev = new Student[marks.length];
    }

    @Override
    public String toString() {
        String res = studentNumber;
        for (int i = 0; i < next.length; i++) {
            res += " (" + String.valueOf(marks[i]) + ":" + next[i].studentNumber + "," + prev[i].studentNumber + ")";
        }
        return res;
    }

    public String listToString(int assignmentIndex) {
        String res = studentNumber + "(" + String.valueOf(marks[assignmentIndex]) + ")";
        Student ptr = next[assignmentIndex];
        while (ptr != this) {
            res += "->" + ptr.studentNumber + "(" + String.valueOf(ptr.marks[assignmentIndex]) + ")";
            ptr = ptr.next[assignmentIndex];
        }
        return res;
    }

    public String listToStringVerbose(int assignmentIndex) {
        String res = toString();
        Student ptr = next[assignmentIndex];
        while (ptr != this) {
            res += "->" + ptr.toString();
            ptr = ptr.next[assignmentIndex];
        }
        return res;
    }
}
