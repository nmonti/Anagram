/* The SuperString object stores the character sorted word, original, and the length.
 * It contains typical getter and setter functions. Nothing fancy here.
 */
public class SuperString {
	private String sorted;
	private String word;
	private int length;
	public SuperString(String sorted, String word, int length){
		this.setSorted(sorted);
		this.setWord(word);
		this.setLength(length);
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getLength() {
		return length;
	}
	public String getSorted() {
		return sorted;
	}
	private void setSorted(String sorted) {
		this.sorted = sorted;
	}
	public String getWord() {
		return word;
	}
	private void setWord(String word) {
		this.word = word;
	}
	
}
