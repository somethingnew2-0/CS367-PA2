
public interface IDblListnode<T> {
	public IDblListnode<T> getNext();
	public IDblListnode<T> getPrev();
	public void setNext(IDblListnode<T> n);
	public void setPrev(IDblListnode<T> p); 
}
