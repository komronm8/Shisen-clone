package tud.ai1.shisen.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Klasse die einen einzelnen Highscore-Eintrag repraesentiert.
 *
 * @author Andrej Felde, Daniel Stein, Nicklas Behler
 *
 */
public class HighscoreEntry implements Comparable<HighscoreEntry> {

	/**
	 * Datum des gespielten Spiels
	 */
	private LocalDateTime date;

	/**
	 * Benoetigte Zeit in Sekunden
	 */
	private double duration;

	/**
	 * Erreichte Punktzahl
	 */
	private int score;

	/**
	 * Separator zum parsen und schreiben der Highscore-Datei
	 */
	private static final String separator = ";";

	/**
	 * Formatter um String in LocalDateTime zu formatieren
	 */
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

	/**
	 * Konstruktor des HighscoreEntrys erzeugt neue Instanz eines
	 * Highscore-Eintrags.
	 *
	 * @param date     Datum des gespielten Spiels.
	 * @param score    Erspielter Score.
	 * @param duration Benoetigte Zeit in Sekunden.
	 */
	public HighscoreEntry(LocalDateTime date, int score, double duration) {
		// TODO Aufgabe 4.1b
		validate(date, score, duration);
		this.date = date;
		this.score = score;
		this.duration = duration;
	}

	/**
	 *
	 */
	public HighscoreEntry(String data) {
		// TODO Aufgabe 4.1c
		String[] lines = data.split(";");
		if(lines.length != 3){
			throw new IllegalArgumentException("Ungültige Parameter gegeben");
		}

		LocalDateTime dateTime = LocalDateTime.parse(lines[0]);
		int score = Integer.parseInt(lines[1]);
		double duration = Double.parseDouble(lines[2]);
		validate(dateTime, score, duration);
		this.date = dateTime;
		this.score = score;
		this.duration = duration;
	}

	/**
	 *
	 */
	public void validate(LocalDateTime date, int score, double duration) {
		// TODO Aufgabe 4.1a
		if (date == null) {
			throw new IllegalArgumentException("Ungültiges Datum: Datum darf nicht null sein.");
		}

		if (duration < 0) {
			throw new IllegalArgumentException("Ungültige Dauer: Dauer darf nicht negativ sein.");
		}

		if (score < 0 || score > 1000) {
			throw new IllegalArgumentException("Ungültiger Score: Score muss zwischen 0 und 1000 liegen.");
		}
	}

	/**
	 *
	 */
	@Override
	public boolean equals(Object obj) {
		// TODO Aufgabe 4.1d

		if (obj == null || !(obj instanceof HighscoreEntry)) {
			return false;
		}
		HighscoreEntry otherHighscore = (HighscoreEntry) obj;
		return this.score == otherHighscore.score && this.duration == otherHighscore.duration && this.date.equals(otherHighscore.date);
	}

	/**
	 * Getter fuer date als String.
	 *
	 * @return String - Spieldatum und Uhrzeit
	 */
	public String getDate() {
		return String.format("%02d.%02d. %02d:%02d", date.getDayOfMonth(), date.getMonthValue(), date.getHour(),
				date.getMinute());
	}

	/**
	 * Getter fuer die Punktzahl.
	 *
	 * @return Erreichte Punktzahl
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Getter fuer die Dauer.
	 *
	 * @return Benoetigte Zeit in Sekunden
	 */
	public double getDuration() {
		return duration;
	}

	/**
	 * Getter, um das Datum in ein Format zum Speichern zu ueberfuehren.
	 *
	 * @return String dd.MM.yyyy hh:mm
	 */
	private String dateToSaveFormat() {
		return String.format("%02d.%02d.%02d %02d:%02d", date.getDayOfMonth(), date.getMonthValue(), date.getYear(),
				date.getHour(), date.getMinute());
	}

	/**
	 *
	 */
	@Override
	public int compareTo(HighscoreEntry other) {
		// TODO Aufgabe 4.1e

		if(this.score < other.score){
			return 1;
		} else if(this.score > other.score){
			return -1;
		}

		if(this.duration < other.duration){
			return -1;
		}
		else if(this.duration > other.duration){
			return 1;
		}
		else{
			return 0;
		}

	}

	/**
	 * Diese Methode gibt die String-Repraesentation des Objekts zurueck.
	 *
	 * @return String-Repraesentation
	 */
	@Override
	public String toString() {
		return dateToSaveFormat() + separator + score + separator + duration;
	}
}
