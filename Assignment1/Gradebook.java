public class Gradebook {
    public String[] assignments;
    public Student[] head;

    public Gradebook(String[] assignments) {
        this.assignments = assignments;
        head = new Student[assignments.length];
    }

    public Gradebook(String inputString) {
        String[] assignmentStrs = inputString.split("\n");
        assignments = new String[assignmentStrs.length];
        head = new Student[assignmentStrs.length];
        for (int i = 0; i < assignments.length; i++) {
            assignments[i] = assignmentStrs[i].split(":")[0];
        }

        Student[] students = new Student[assignmentStrs[0].split(":")[1].split("->").length];
        int c = 0;
        for (String s : assignmentStrs[0].split(":")[1].split("->")) {
            students[c++] = new Student(s.split("\\(")[0], new int[assignmentStrs.length]);
        }

        for (int a = 0; a < assignments.length; a++) {
            String list = assignmentStrs[a].split(":")[1];
            Student ptr = null;
            for (String target : list.split("->")) {
                for (Student s : students) {
                    if (s.studentNumber.equals(target.split("\\(")[0])) {
                        s.marks[a] = Integer.valueOf(target.split("\\(")[1].replace(")", ""));
                        if (ptr == null) {
                            head[a] = s;
                            ptr = s;
                        } else {
                            ptr.next[a] = s;
                            s.prev[a] = ptr;
                            ptr = s;
                        }
                    }
                }
            }
            ptr.next[a] = head[a];
            head[a].prev[a] = ptr;
        }
    }

    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < assignments.length; i++) {
            res += assignments[i] + ":" + (head[i] == null ? "" : head[i].listToString(i)) + "\n";
        }
        return res;
    }

    public String toStringVerbose() {
        String res = "";
        for (int i = 0; i < assignments.length; i++) {
            res += assignments[i] + ":" + (head[i] == null ? "" : head[i].listToStringVerbose(i)) + "\n";
        }
        return res;
    }

    /*
     * Everything above this line was given to you and should not be changed
     */

    public void addStudent(String studentNumber, int[] marks) {
    }

    public void removeStudent(String studentNumber) {
    }

    public int[] getMarks(String studentNumber) {
    }

    public void updateStudent(String studentNumber, String assignment, int mark) {
    }

    public void addAssignment(String assignmentName) {
    }

    public void removeAssignment(String assignmentName) {
    }

    public String assignmentStats(String assingment) {
    }

    public String studentStats(String student) {
    }
}
