package tud.ai1.shisen.model;

import tud.ai1.shisen.util.IOOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse, welche für die Verwaltung der Highscores verantworlicht ist. Laedt,
 * speichert und legt neue Highscore-Eintraege in einer ArrayList an und
 * speichert die ArrayListen zusammen mit dem Key der passenden Feldgröße in
 * eine Hashmap.
 *
 * @author Andrej Felde, Daniel Stein, Nicklas Behler
 */
public class Highscore {

	/**
	 * Maximale Anzahl an Highscore-Eintraegen in einer Highscore-Liste pro
	 * Feldgroeße.
	 */
	public static final int MAX_ENTRIES = 10;

	/**
	 * Alle Highscore-Listen mit zugehoeriger Levelgroeße
	 */
	private List<HighscoreEntry> highscores;

	/**
	 * Konstruktor der Highscore-Klasse.
	 */
	public Highscore() {
		highscores = new ArrayList<HighscoreEntry>();
	}

	/**
	 * Methode zum Erzeugen der Highscores aus dem gespeicherten Stringformat. Teilt
	 * den uebergebenen String in einzelne Eintraege und speichert diese in einem
	 * String-Array. Danach werden die einzelnen Eintraege in der Hashmap, in die
	 * passenden Arraylists ihrer Feldgroeße einsortiert.
	 *
	 * @param str String, der die vorherigen Highscores enthaelt.
	 */
	public void initHighscore(String str) {
		if (str.isEmpty())
			return;

		for (String line : str.split("\\r?\\n")) {
			addEntry(new HighscoreEntry(line));
		}
	}

	/**
	 * 
	 */
	public void saveToFile(String fileName) {
		// TODO Aufgabe 4.2b
		StringBuilder data = new StringBuilder();
		for(HighscoreEntry score: getHighscore()){
			data.append(score);
			data.append(System.lineSeparator());
		}
		if(data.length() != 0){
			IOOperations.writeFile(fileName, String.valueOf(data));
		}
	}

	/**
	 * Getter Methode für Highscores einzelner Level.
	 *
	 * @return Gibt die Highscore-Liste zurueck
	 */
	public List<HighscoreEntry> getHighscore() {
		return highscores;
	}

	/**
	 * 
	 */
	public void addEntry(HighscoreEntry entry) {
		// TODO Aufgabe 4.2a
		if(this.highscores.size() == 10){
			if(entry.getScore() > this.highscores.get(9).getScore() ){
				for(int i = 8; i >= 0; i--){
					if(entry.getScore() < this.highscores.get(i).getScore()){
						this.highscores.remove(9);
						this.highscores.add(i+1, entry);
						return;
					}
				}
			}
		}
		else{
			for(int i = 0; i < this.highscores.size(); i++){
				if(entry.getScore() > this.highscores.get(i).getScore()){
					this.highscores.add(i, entry);
					return;
				}
			}
		}

	}
}
