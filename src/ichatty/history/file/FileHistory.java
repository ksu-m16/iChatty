package ichatty.history.file;

import ichatty.common.IMessage;
import ichatty.history.IHistory;
import ichatty.serialize.IMessageSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

class FileHistory implements IHistory {				
	File file;
	IMessageSerializer serializer;
	
	public FileHistory(String fileName, IMessageSerializer serializer) {
		this.serializer = serializer;
		this.file = new File(fileName);			
	}
	
	
	@Override
	public List<IMessage> getMessages() throws IOException {
		List<IMessage> list = new LinkedList<IMessage>();
		if (!file.canRead()) {
			return list;
		}

		BufferedReader r = null;	
		try {
			r = new BufferedReader(new InputStreamReader(new FileInputStream(file)));					
			while (r.ready()) {
				String line = r.readLine();				
				list.add(serializer.deserialize(line));
			}
		} finally {
			if (r != null) {
				r.close();
			}
		}
		
		return list;
	}

	@Override
	public void addMessage(IMessage m) throws IOException {
		if (!file.getParentFile().isDirectory()) {
			file.getParentFile().mkdirs();
		}
		
		PrintWriter w = null;
		try {
			w = new PrintWriter(new FileOutputStream(file, true));		
			w.println(serializer.serialize(m));
		} finally {
			if (w != null) {
				w.close();
			}
		}
	} 	
}
