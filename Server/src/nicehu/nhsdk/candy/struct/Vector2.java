package nicehu.nhsdk.candy.struct;

public class Vector2<X, Y>
{
	public X x;
	public Y y;

	public Vector2()
	{
	}

	public Vector2(X x, Y y)
	{
		this.x = x;
		this.y = y;
	}

	public X getX()
	{
		return x;
	}

	public void setX(X x)
	{
		this.x = x;
	}

	public Y getY()
	{
		return y;
	}

	public void setY(Y y)
	{
		this.y = y;
	}

}
