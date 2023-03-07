import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class EditableBufferedReader extends BufferedReader {

	private InputStreamReader in;
	private char[] buffer;
	private final int DEFAULT_LENGTH = 256;
	private int cursor;

	public EditableBufferedReader(InputStreamReader in) {
		super(in);
	}

	/*
	 * Passa la consola de mode cooked a mode raw.
	 */
	public void setRaw() {
	}

	/*
	 * Passa la consola de raw cooked a mode cooked.
	 */
	public void unsetRaw() {
	}

	/*
	 * Llegeix el següent caràcter o la següent tecla de cursor.
	 */
	public int read() {
		return 0;
	}

	/*
	 * Llegeix la línia amb possibilitat d’editar-la.
	 */
	public String readLine() throws IOException {
		String buffer = new String();
		char newChar;
		do {
			newChar = (char) super.read();
			buffer += newChar;
		} while (newChar != '\n');
		return buffer;
	}
}