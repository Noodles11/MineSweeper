package saper;

public class KrokAI{

	public int number;
	public int x,y, clicks;
	public String opis;
	public boolean lpm, ppm;

	public KrokAI(int n, int x, int y, boolean lpm, boolean ppm, int clicks, String opis){
		this.number = n;

		this.x = x;
		this.y = y;

		this.lpm = lpm;
		this.ppm = ppm;

		this.clicks = clicks;

		this.opis = opis;
	}

    public boolean equals(Object o)
    {
        boolean identyczny = false;

        if (o != null)
        {
        	if( this.x==((KrokAI) o).x && this.y==((KrokAI) o).y && this.lpm==((KrokAI) o).lpm && this.ppm==((KrokAI) o).ppm && this.clicks==((KrokAI) o).clicks )
        		identyczny = true;
        }

        return identyczny;
    }
}