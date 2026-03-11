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
        Student newStudent = new Student(studentNumber, marks);
        for (int i = 0; i < assignments.length; i++) {
            insertIntoList(newStudent, i);
    }

    private void insertIntoList(Student s, int i) {
        if (head[i] == null) {
            head[i] = s;
            s.next[i] = s;
            s.prev[i] = s;
            return;
        }

        // Find insertion point: first node whose mark is LESS than s,
        // or (same mark AND studentNumber > s.studentNumber)
        Student cur = head[i];
        Student insertBefore = null;
        do {
            if (cur.marks[i] < s.marks[i]) {
                insertBefore = cur;
                break;
            }
            if (cur.marks[i] == s.marks[i] && cur.studentNumber.compareTo(s.studentNumber) > 0) {
                insertBefore = cur;
                break;
            }
            cur = cur.next[i];
        } while (cur != head[i]);

        if (insertBefore == null) {
            // s goes at the end (tail), i.e. just before head
            insertBefore = head[i]; // insert before head = append at tail
            // but we DON'T update head[i]
            Student tail = head[i].prev[i];
            tail.next[i] = s;
            s.prev[i] = tail;
            s.next[i] = head[i];
            head[i].prev[i] = s;
        } else {
            // insert before insertBefore
            Student prev = insertBefore.prev[i];
            prev.next[i] = s;
            s.prev[i] = prev;
            s.next[i] = insertBefore;
            insertBefore.prev[i] = s;
            if (insertBefore == head[i]) {
                head[i] = s;
            }
        }
    }

    public void removeStudent(String studentNumber) {
        if (assignments.length == 0) return;

       
        Student target = findStudent(studentNumber);
        if (target == null) return;

        for (int i = 0; i < assignments.length; i++) {
            removeFromList(target, i);
        }
    }

    private void removeFromList(Student s, int i) {
        if (head[i] == null) return;

        if (s.next[i] == s) {
           
            head[i] = null;
        } else {
            s.prev[i].next[i] = s.next[i];
            s.next[i].prev[i] = s.prev[i];
            if (head[i] == s) {
                head[i] = s.next[i];
            }
        }
        s.next[i] = null;
        s.prev[i] = null;
    }

    public int[] getMarks(String studentNumber) {

        Student s = findStudent(studentNumber);
        return (s == null) ? null : s.marks;
    }

    public void updateStudent(String studentNumber, String assignment, int mark) {
         int idx = findAssignment(assignment);
        if (idx == -1) return;

        Student s = findStudent(studentNumber);
        if (s == null) return;

        // Remove from that one list, update mark, re-insert
        removeFromList(s, idx);
        s.marks[idx] = mark;
        insertIntoList(s, idx);
    }

    public void addAssignment(String assignmentName) {

        int oldLen = assignments.length;
        int newIdx = oldLen; // index of the new assignment

        // --- grow assignments[] ---
        String[] newAssignments = new String[oldLen + 1];
        for (int i = 0; i < oldLen; i++) newAssignments[i] = assignments[i];
        newAssignments[newIdx] = assignmentName;
        assignments = newAssignments;

        // --- grow head[] ---
        Student[] newHead = new Student[oldLen + 1];
        for (int i = 0; i < oldLen; i++) newHead[i] = head[i];
        newHead[newIdx] = null;
        head = newHead;

        // --- collect all students (if any lists exist) ---
        // Count students first
        if (oldLen == 0 || head[0] == null) {
            // No existing students — nothing more to do
            return;
        }

        // Count students in list 0
        int count = 0;
        Student cur = head[0];
        do { count++; cur = cur.next[0]; } while (cur != head[0]);

        // Collect them into a temporary array (no imports allowed)
        Student[] students = new Student[count];
        cur = head[0];
        for (int k = 0; k < count; k++) {
            students[k] = cur;
            cur = cur.next[0];
        }

        // --- grow each student's arrays ---
        for (int k = 0; k < count; k++) {
            Student s = students[k];
            int[] newMarks = new int[oldLen + 1];
            Student[] newNext = new Student[oldLen + 1];
            Student[] newPrev = new Student[oldLen + 1];
            for (int j = 0; j < oldLen; j++) {
                newMarks[j] = s.marks[j];
                newNext[j]  = s.next[j];
                newPrev[j]  = s.prev[j];
            }
            newMarks[newIdx] = 0;   // new mark defaults to 0
            newNext[newIdx]  = null;
            newPrev[newIdx]  = null;
            s.marks = newMarks;
            s.next  = newNext;
            s.prev  = newPrev;
        }

        // --- sort students ascending by studentNumber (all marks == 0) ---
        // Simple insertion sort (no imports)
        for (int a = 1; a < count; a++) {
            Student key = students[a];
            int b = a - 1;
            while (b >= 0 && students[b].studentNumber.compareTo(key.studentNumber) > 0) {
                students[b + 1] = students[b];
                b--;
            }
            students[b + 1] = key;
        }

        // --- build new circular doubly-linked list at newIdx ---
        for (int k = 0; k < count; k++) {
            students[k].next[newIdx] = students[(k + 1) % count];
            students[k].prev[newIdx] = students[(k - 1 + count) % count];
        }
        head[newIdx] = students[0];
    }

    public void removeAssignment(String assignmentName) {

        int idx = findAssignment(assignmentName);
        if (idx == -1) return;

        int oldLen = assignments.length;
        int newLen = oldLen - 1;

        // Collect students before we destroy any array (use any surviving list,
        // or the list being removed if it's the only one).
        Student[] students = collectAllStudents(idx);
        int count = students.length;

        // --- shrink assignments[] ---
        String[] newAssignments = new String[newLen];
        for (int i = 0, j = 0; i < oldLen; i++) {
            if (i != idx) newAssignments[j++] = assignments[i];
        }
        assignments = newAssignments;

        // --- shrink head[] ---
        Student[] newHead = new Student[newLen];
        for (int i = 0, j = 0; i < oldLen; i++) {
            if (i != idx) newHead[j++] = head[i];
        }
        head = newHead;

        // --- shrink each student's arrays ---
        for (int k = 0; k < count; k++) {
            Student s = students[k];
            int[] newMarks = new int[newLen];
            Student[] newNext = new Student[newLen];
            Student[] newPrev = new Student[newLen];
            for (int i = 0, j = 0; i < oldLen; i++) {
                if (i != idx) {
                    newMarks[j] = s.marks[i];
                    newNext[j]  = s.next[i];
                    newPrev[j]  = s.prev[i];
                    j++;
                }
            }
            s.marks = newMarks;
            s.next  = newNext;
            s.prev  = newPrev;
        }
    }

    public String assignmentStats(String assingment) {

        int idx = findAssignment(assignment);
        if (idx == -1) return "";

        String name = assignments[idx];

        if (head[idx] == null) {
            return name + " stats\nMin:-\nMax:-\nAvg:-\n";
        }

        // head[idx] = max (list sorted descending), head[idx].prev[idx] = min
        int max = head[idx].marks[idx];
        int min = head[idx].prev[idx].marks[idx];

        // Traverse for average
        long sum = 0;
        int count = 0;
        Student cur = head[idx];
        do {
            sum += cur.marks[idx];
            count++;
            cur = cur.next[idx];
        } while (cur != head[idx]);

        double avg = (double) sum / count;
        return name + " stats\nMin:" + min + "\nMax:" + max + "\nAvg:" + String.format("%.2f", avg) + "\n";
    }

    public String studentStats(String student) {

        Student s = findStudent(studentNumber);
        if (s == null) return "";

        if (assignments.length == 0) {
            return studentNumber + " stats\nMin:-\nMax:-\nAvg:-\nBest Position:-\nWorst Position:-\n";
        }

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        long sum = 0;
        int bestPos = Integer.MAX_VALUE;
        int worstPos = Integer.MIN_VALUE;

        for (int i = 0; i < assignments.length; i++) {
            int m = s.marks[i];
            if (m < min) min = m;
            if (m > max) max = m;
            sum += m;

            // Count position (1-based) in list i
            int pos = 1;
            Student cur = head[i];
            while (cur != s) {
                pos++;
                cur = cur.next[i];
            }
            if (pos < bestPos)  bestPos  = pos;
            if (pos > worstPos) worstPos = pos;
        }

        double avg = (double) sum / assignments.length;
        return studentNumber + " stats\nMin:" + min + "\nMax:" + max
             + "\nAvg:" + String.format("%.2f", avg)
             + "\nBest Position:" + bestPos
             + "\nWorst Position:" + worstPos + "\n";
    }
}


private Student findStudent(String studentNumber) {
       
        for (int i = 0; i < assignments.length; i++) {
            if (head[i] != null) {
                Student cur = head[i];
                do {
                    if (cur.studentNumber.equals(studentNumber)) return cur;
                    cur = cur.next[i];
                } while (cur != head[i]);
                return null; 
            }
        }
        return null;
    }

    private int findAssignment(String name) {
        for (int i = 0; i < assignments.length; i++) {
            if (assignments[i].equals(name)) return i;
        }
        return -1;
    }

  
    private Student[] collectAllStudents(int preferredIdx) {
        int listIdx = -1;
        if (preferredIdx >= 0 && preferredIdx < head.length && head[preferredIdx] != null) {
            listIdx = preferredIdx;
        } else {
            for (int i = 0; i < head.length; i++) {
                if (head[i] != null) { listIdx = i; break; }
            }
        }
        if (listIdx == -1) return new Student[0];

        int count = 0;
        Student cur = head[listIdx];
        do { count++; cur = cur.next[listIdx]; } while (cur != head[listIdx]);

        Student[] arr = new Student[count];
        cur = head[listIdx];
        for (int k = 0; k < count; k++) {
            arr[k] = cur;
            cur = cur.next[listIdx];
        }
        return arr;
    }
}