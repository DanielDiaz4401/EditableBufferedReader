import java.io.*;
import java.lang.reflect.Constructor;

class Line {

    private static final int DEFAULT_LENGTH = 16;

    private static final int INSERT = 1;
    private static final int APPEND = 2;

    private StringBuffer buffer;
    private int cursor;
    private int mode;

    /*
     * Constructor
     */
    public Line() {
        this.buffer = new StringBuffer(DEFAULT_LENGTH);
        this.cursor = 0;
        this.mode = APPEND;
    }

    /*
     * adds character at the end
     * 
     * relPosition: positive or negative number of positions to move
     */
    public void addCharacter(char character) {
        if (this.buffer.length() == this.buffer.capacity()) { // 16 is the default length of the StringBuffer class
            StringBuffer aux = new StringBuffer(this.buffer.capacity() + DEFAULT_LENGTH);
            aux.append(buffer);
            buffer = aux;
        }
        this.buffer.insert(cursor, character);
        cursor++;
    }

    /*
     * Moves cursor relative to the actual position
     * 
     * relPosition: positive or negative number of positions to move
     */
    public void move(int relPosition) {
        int newCursor = this.cursor + relPosition;
        if (newCursor < 0 || newCursor >= buffer.length()) {
            return;
        }
        this.cursor += relPosition;
    }

    public void moveTo(int position) {
        if (position >= buffer.length())
            if (position == -1) {
                this.cursor = buffer.length() - 1;
            }
        this.cursor = position;
    }

    public String toString() {
        return this.buffer.toString();

    }

    public String toStringDebug() {
        String aux = "";
        for (int i = 0; i < buffer.length(); i++) {
            aux += (int) buffer.charAt(i) + ", ";
        }
        return aux;
    }
}