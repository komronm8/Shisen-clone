package tud.ai1.shisen.model;

import java.util.Arrays;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;

import tud.ai1.shisen.util.Consts;
import tud.ai1.shisen.util.IOOperations;
import tud.ai1.shisen.util.PathFinder;

/**
 * Diese Klasse repraesentiert das Spielfeld.
 * 
 * @author Nicklas Behler, Sebastian C, Lennart Fedler, Niklas Grimm, Robert
 *         Jakobi, Max Kratz, Niklas Vogel
 *
 */
public class Grid implements IGrid {

	private int waitTime = 1000;
	private TokenState destiny;
	private long currTime;
	private boolean timerActive = false;
	private List<IToken> list;
	private static int score = 0;

	/**
	 * TODO: Aufgabe 3a
	 */
	private static IToken[][] grid;
	private IToken selectedTokenOne = null;
	private IToken selectedTokenTwo = null;


	/**
	 * Konstruktor, der ein zufaelliges Grid zum Testen erzeugt.
	 */
	public Grid() {
		final IToken[][] demoGrid = new IToken[10][10];
		for (int x = 0; x < demoGrid.length; x++) {
			for (int y = 0; y < demoGrid[x].length; y++) {
				demoGrid[x][y] = new Token(1);
			}
		}
		grid = demoGrid;
	}

	/**
	 * TODO: Aufgabe 3b
	 */
	public Grid(String mapPath){
		grid = parseMap(mapPath);
		fillTokenPositions();
		score = 0;
	}

	/**
	 * TODO: Aufgabe 3c
	 */
	@Override
	public IToken getTokenAt(int x, int y) {
		if (x >= 0 && x < grid.length && y >= 0 && y < grid[0].length) {
			return grid[x][y];
		} else {
			return null;
		}
	}

	/**
	 * TODO: Aufgabe 3d
	 */
	@Override
	public IToken[][] getGrid() {
		return grid;
	}

	@Override
	public IToken[] getActiveTokens() {
		return new IToken[]{this.selectedTokenOne, this.selectedTokenTwo};
	}

	/**
	 * TODO: Aufgabe 3e
	 */
	@Override
	public boolean bothClicked() {
		return this.selectedTokenOne != null && this.selectedTokenTwo != null;
	}

	/**
	 * TODO: Aufgabe 3f
	 */
	@Override
	public void deselectToken(IToken token) {
		if (token == selectedTokenOne) {
			selectedTokenOne.setTokenState(TokenState.DEFAULT);
			selectedTokenOne = null;
		} else if (token == selectedTokenTwo) {
			selectedTokenTwo.setTokenState(TokenState.DEFAULT);
			selectedTokenTwo = null;
		}
	}

	@Override
	public void deselectTokens() {
		if (selectedTokenOne != null) {
			selectedTokenOne.setTokenState(TokenState.DEFAULT);
			selectedTokenOne = null;
		}
		if (selectedTokenTwo != null) {
			selectedTokenTwo.setTokenState(TokenState.DEFAULT);
			selectedTokenTwo = null;
		}
	}

	/**
	 * TODO: Aufgabe 3g
	 */
	public static boolean isSolved(){
		for (int x = 0; x < grid.length; x++){
			for (int y = 0; y < grid[0].length; y++){
				if (grid[x][y].getTokenState() != TokenState.SOLVED){
					return false;
				}
			}
		}
		return true;
	}


	/**
	 * TODO: Aufgabe 3h
	 */
	private Token[][] parseMap(String path){

		String map = IOOperations.readFile(path);
		String[] lines = map.split(System.lineSeparator());

		int cols = 10;
		int rows = 20;
		int[][] integerMap = new int[18][8];

		for(int i = 0; i < lines.length; i++){
			integerMap[i] =  Arrays.stream(lines[i].split(",")).mapToInt(Integer::parseInt).toArray();
		}


		Token[][] grid = new Token[rows][cols];
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				if(i == 0 || i == 19){
					grid[i][j] = new Token(TokenState.SOLVED, -1, new Vector2f(i, j));
				} else if(j == 0 || j == 9){
					grid[i][j] = new Token(TokenState.SOLVED, -1, new Vector2f(i, j));
				} else{
					grid[i][j] = new Token(TokenState.DEFAULT, integerMap[j-1][i-1], new Vector2f(i, j));
				}
			}
		}

		String[] split = ("04.01.1996 07:07;123;851256.0").split(";");
		System.out.println(Arrays.toString(split));

		return grid;
	}


	/**
	 * Updated den Score um incr. Sollte der Score anschliessend negativ sein, so
	 * wird er bis auf 0 dekrementiert.
	 *
	 * @param incr Zahl um die Score erhoeht / erniedrigt werden soll.
	 */
	public void updateScore(final int incr) {
		if (score + incr >= 0) {
			score += incr;
		} else {
			score = 0;
		}
	}

	/**
	 * Getter fuer score.
	 *
	 * @return Aktueller score.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Teile jedem Token seine Position im Array mit.
	 */
	private void fillTokenPositions() {
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				grid[x][y].setPos(new Vector2f(x, y));
			}
		}
	}

	/**
	 * Waehle einen Token auf dem Spielfeld aus und loese diesen falls moeglich.
	 *
	 * @param token Angeklickter Token.
	 */
	@Override
	public void selectToken(final IToken token) {
		if (this.selectedTokenOne == null) {
			this.selectedTokenOne = token;
			selectedTokenOne.setTokenState(TokenState.CLICKED);
		} else if (this.selectedTokenTwo == null) {
			this.selectedTokenTwo = token;
			selectedTokenTwo.setTokenState(TokenState.CLICKED);
			this.list = PathFinder.getInstance().findPath(this, (int) this.selectedTokenOne.getPos().x,
					(int) this.selectedTokenOne.getPos().y, (int) this.selectedTokenTwo.getPos().x,
					(int) this.selectedTokenTwo.getPos().y);
			if (this.list == null || this.list.size() == 0
					|| !this.selectedTokenOne.getDisplayValue().equals(this.selectedTokenTwo.getDisplayValue())) {
				this.selectedTokenOne.setTokenState(TokenState.WRONG);
				this.selectedTokenTwo.setTokenState(TokenState.WRONG);
				this.updateScore(Consts.DECREASE_SCORE);
				this.startTimer(Consts.DISPLAY_WRONG_TIME, TokenState.DEFAULT);
			} else {
				for (final IToken tok : this.list) {
					tok.setTokenState(TokenState.CLICKED);
				}
				this.updateScore(Consts.GAIN_SCORE);
				this.startTimer(Consts.DISPLAY_WRONG_TIME, TokenState.SOLVED);
			}
		}
	}

	/**
	 * Startet einen Timer (Genutzt fuer Anzeigedauer bei falscher / richtiger
	 * Auswahl von zwei Tokens).
	 *
	 * @param waitTime Zeit in Sekunden, die gewartet werden soll.
	 * @param dest     Ziel Tokenstate.
	 */
	private void startTimer(final double waitTime, final TokenState dest) {
		this.timerActive = true;
		this.currTime = System.currentTimeMillis();
		this.waitTime = (int) waitTime * 1000;
		this.destiny = dest;
	}

	/**
	 * Prueft ob Anzeigezeit bei falscher/richtiger Auswahl bereits ueberschritten
	 * ist. Falls ja wird der entsprechende Code ausgefuehrt.
	 */
	@Override
	public void getTimeOver() {
		if (this.timerActive) {
			if (System.currentTimeMillis() - this.currTime > this.waitTime) {
				try {
					if (this.list != null) {
						for (final IToken tok : this.list) {
							tok.setTokenState(TokenState.SOLVED);
						}
					}
					this.selectedTokenOne.setTokenState(this.destiny);
					this.selectedTokenTwo.setTokenState(this.destiny);
					this.selectedTokenOne = null;
					this.selectedTokenTwo = null;
					this.timerActive = false;
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
