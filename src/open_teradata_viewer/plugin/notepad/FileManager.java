/*
 * Open Teradata Viewer ( notepad plugin )
 * Copyright (C) 2012, D. Campione
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package open_teradata_viewer.plugin.notepad;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

/**
 * Read and write a file using an explicit encoding.
 * Removing the encoding from this code will simply cause the system's default
 * encoding to be used instead.
 * 
 * @author D. Campione
 */
public final class FileManager {

    private final String fFileName;

    private final String fEncoding;

    /** Ctor. */
    public FileManager(String aFileName, String aEncoding) {
        fEncoding = aEncoding;
        fFileName = aFileName;
    }

    /** Write fixed content to the given file. */
    public void write(String text) throws IOException {
        Writer out = new OutputStreamWriter(new FileOutputStream(fFileName),
                fEncoding);
        out.write(text);
        out.close();
    }

    /** Read the contents of the given file. */
    public String read() throws IOException {
        StringBuilder text = new StringBuilder();
        String NL = System.getProperty("line.separator");
        Scanner scanner = new Scanner(new FileInputStream(fFileName), fEncoding);
        try {
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine() + NL);
            }
        } finally {
            scanner.close();
        }
        return text.toString();
    }
}