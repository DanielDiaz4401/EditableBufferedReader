import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

class TestReadMultiLine {
    public static void main(String[] args) {
        BufferedReader in = new EditableBufferedReader(
                new InputStreamReader(System.in));
        String str = null;
        try {
            str = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("\033[32m"); // Canvia el color a verd per mostrar el resultat final
        System.out.println("\n\033[1Gline is: " + str);
        System.out.print("\033[1G\033[0m"); // Canvia de línia, es posa a la primera posició i torna al color per
                                            // defecte
    }
}

class Console {

    public void updateView(Lines lines, int cursor) {
        System.out.print("\033[2K\033[2;1H");
        String[] array = lines.toStrings();
        for (int i = 0; i < array.length; i++) {
            System.out.println("\033[" + (i + 1) + "\033[" + (i + 1) + ";1H" + array[i]);
        }
        String cursorPosition = "\033[" + lines.getNumber() + ";" + cursor + "H";
    }

    public void abort() {
        System.out.print("\033[3;1H"); // Posa el cursor a la 3a linia
        System.out.print("\033[31m"); // Canvia el color a vermell per mostrar l'error
        System.out.print("Aborted");
        System.out.print("\033[0m"); // Torna al color per defecte
        System.out.print("\033[4;1H"); // Coloca el cursor a la 4a línia
    }

}

class EditableBufferedReader extends BufferedReader {

    private InputStreamReader in;
    private Console out;
    private Line buffer;
    private int cursor;

    public EditableBufferedReader(InputStreamReader in) {
        super(in);
        this.out = new Console();
        this.cursor = 1;
        this.buffer = new Line();
    }

    /*
     * Passa la consola de mode cooked a mode raw.
     */
    public void setRaw() {
        try {
            String[] cmd = { "/bin/sh", "-c", "stty raw -echo </dev/tty" };
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Passa la consola de raw cooked a mode cooked.
     */
    public void unsetRaw() {
        try {
            String[] cmd = { "/bin/sh", "-c", "stty cooked echo </dev/tty" };
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Llegeix el següent caràcter o la següent tecla de cursor.
     */
    public int read() throws IOException {
        int newChar = super.read();
        return newChar;
    }

    /*
     * Llegeix la línia amb possibilitat d’editar-la.
     */
    public String readLine() throws IOException {
        setRaw();
        System.out.print("\033[4L");
        Lines buffer = new Lines();
        char newChar;
        int cursor = 1;
        do {
            newChar = (char) read();
            System.out.println((int) newChar);
            switch (newChar) {
                case (char) 3: // Control+C -> End program succesfully
                    unsetRaw();
                    return buffer.toString();
                case (char) 4: // Control+D -> EOT (End Of Transmission)
                    unsetRaw();
                    out.updateView(buffer, cursor);
                    out.abort();
                    System.exit(1);
                case (char) 27: // ^
                    controlSequence(buffer);
                    break;
                case (char) 13: // \n
                    buffer.endLine();
                    cursor = 1;
                    out.updateView(buffer, cursor);
                    break;
                case (char) 1: // delete
                    buffer.delete(cursor);
                    cursor = cursor == 1 ? cursor : cursor--;
                    out.updateView(buffer, cursor);
                    break;
                default:
                    buffer.addChar(newChar, cursor);
                    cursor++;
                    out.updateView(buffer, cursor);
            }
        } while (true);
    }

    public void controlSequence(Lines buffer) throws IOException {
        char newchar = (char) read();
        switch (newchar) {
            case (char) 91: // [
                escapeSequence(buffer);
                break;

            default:
                break;
        }
    }

    void escapeSequence(Lines buffer) throws IOException {
        char newchar = (char) read();
        switch (newchar) {
            case 68: // <-
                cursor--;
                System.out.print("\033[1D");
                break;
            case 69: // ->
                cursor++;
                System.out.print("\033[1C");
                break;
            default:
                break;
        }
    }
}

class Line {
    private String str;

    public Line() {
        this.str = "";
    }

    public String delete(int cursor) {
        if (cursor > 1)
            str = str.substring(0, cursor - 2) + str.substring(cursor - 1);
        return str;
    }

    public String addChar(int c, int cursor) {
        str = str.substring(0, cursor - 1) + (char) c + str.substring(cursor - 1);
        return str;
    }

    public String toString() {
        return this.str;
    }
}

class Lines {

    private List<Line> lines;
    private Line lastLine;

    public Lines() {
        this.lines = new LinkedList<>();
        lastLine = new Line();
        lines.add(lastLine);
    }

    public List<Line> endLine() {
        lastLine = new Line();
        this.lines.add(lastLine);
        return lines;
    }

    public List<Line> delete(int cursor) {
        lastLine.delete(cursor);
        return lines;
    }

    public List<Line> addChar(char c, int cursor) {
        lastLine.addChar(c, cursor);
        return lines;
    }

    public int getNumber() {
        return lines.size();
    }

    public String toString() {
        String res = "";
        for (Line line : lines) {
            res += line + "\n";
        }
        return res;
    }

    public String[] toStrings() {
        String[] res = new String[lines.size()];
        Iterator<Line> it = lines.iterator();
        for (int i = 0; i < res.length; i++) {
            res[i] = it.next().toString();
        }
        return res;
    }
}