
public class Pair <E,V>{
	private E e;
	private V v;
	Pair(E e, V v){
		this.setE(e);
		this.setV(v);
	}
	public E getE() {
		return e;
	}
	public void setE(E e) {
		this.e = e;
	}
	public V getV() {
		return v;
	}
	public void setV(V v) {
		this.v = v;
	}
	public void print(){
		System.out.println(this.e + " " + this.v);
	}
}
