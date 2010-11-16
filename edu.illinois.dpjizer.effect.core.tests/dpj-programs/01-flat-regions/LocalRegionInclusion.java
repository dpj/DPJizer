/**
 * @author Mohsen Vakilian
 */
class C<region R> {
	public void m() {
		region r;
		C<Local:*> c = new C<r>();
	}
}