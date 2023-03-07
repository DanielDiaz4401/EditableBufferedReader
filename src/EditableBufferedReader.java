import java.io.*;
import java.util.regex.*;

class EditableBufferedReader extends BufferedReader {

	private InputStreamReader in;
	private Line buffer;

	public EditableBufferedReader(InputStreamReader in) {
		super(in);
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
		Line buffer = new Line();
		char newChar;
		do {
			newChar = (char) read();
			// System.out.println((int) newChar);
			switch (newChar) {
				case (char) 13:
					unsetRaw();
					return buffer.toString();
				case (char) 4: // Control+D -> EOT (End Of Transmission)
					unsetRaw();
					System.err.println("Aborted");
					System.exit(1);
				case (char) 27: // ^
					controlSequence(buffer);
					break;
				default:
					buffer.addCharacter(newChar);
					System.out.print(newChar);
			}
		} while (true);
	}

	public void controlSequence(Line buffer) throws IOException {
		char newchar = (char) read();
		switch (newchar) {
			case (char) 91: // [
				escapeSequence(buffer);	
				break;
		
			default:
				break;
		}
	}

	public void escapeSequence(Line buffer) throws IOException {
		char newchar = (char) read();
		switch (newchar) {
			case 68: // <-
				buffer.move(-1);	
				System.out.print("\033[1D");
				break;
			case 69: // ->
				buffer.move(1);	
				System.out.print("\033[1C");
				break;
			default:
				break;
		}
	}
}
/*
 * up ^[[A
 * down ^[[B
 * -> ^[[C
 * <- ^[[D
 */
