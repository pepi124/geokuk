package cz.geokuk.plugins.kesoidpopisky;

import java.awt.Color;
import java.awt.Font;

import cz.geokuk.framework.Copyable;
import cz.geokuk.framework.Preferenceble;

@Preferenceble
public class PopiskySettings implements Copyable<PopiskySettings> {

	public Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
	public Color foreground = Color.BLACK;
	public Color background = new Color(255, 255, 255, 70);

	public int posuX = 0;
	public int posuY = 0;

	public PopiskyPatterns patterns = new PopiskyPatterns();

	@Override
	public PopiskySettings copy() {
		try {
			final PopiskySettings klon = (PopiskySettings) super.clone();
			klon.setPatterns(klon.getPatterns().copy());
			return klon;
		} catch (final CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PopiskySettings other = (PopiskySettings) obj;
		if (background == null) {
			if (other.background != null) {
				return false;
			}
		} else if (!background.equals(other.background)) {
			return false;
		}
		if (font == null) {
			if (other.font != null) {
				return false;
			}
		} else if (!font.equals(other.font)) {
			return false;
		}
		if (foreground == null) {
			if (other.foreground != null) {
				return false;
			}
		} else if (!foreground.equals(other.foreground)) {
			return false;
		}
		if (patterns == null) {
			if (other.patterns != null) {
				return false;
			}
		} else if (!patterns.equals(other.patterns)) {
			return false;
		}
		if (posuX != other.posuX) {
			return false;
		}
		if (posuY != other.posuY) {
			return false;
		}
		return true;
	}

	/**
	 * @return the background
	 */
	public Color getBackground() {
		return background;
	}

	/**
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * @return the foreground
	 */
	public Color getForeground() {
		return foreground;
	}

	/**
	 * @return the patterns
	 */
	public PopiskyPatterns getPatterns() {
		return patterns;
	}

	/**
	 * @return the posuX
	 */
	public int getPosuX() {
		return posuX;
	}

	/**
	 * @return the posuY
	 */
	public int getPosuY() {
		return posuY;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (background == null ? 0 : background.hashCode());
		result = prime * result + (font == null ? 0 : font.hashCode());
		result = prime * result + (foreground == null ? 0 : foreground.hashCode());
		result = prime * result + (patterns == null ? 0 : patterns.hashCode());
		result = prime * result + posuX;
		result = prime * result + posuY;
		return result;
	}

	/**
	 * @param background
	 *            the background to set
	 */
	public void setBackground(final Color background) {
		this.background = background;
	}

	/**
	 * @param font
	 *            the font to set
	 */
	public void setFont(final Font font) {
		this.font = font;
	}

	/**
	 * @param foreground
	 *            the foreground to set
	 */
	public void setForeground(final Color foreground) {
		this.foreground = foreground;
	}

	/**
	 * @param patterns
	 *            the patterns to set
	 */
	public void setPatterns(final PopiskyPatterns patterns) {
		this.patterns = patterns;
	}

	/**
	 * @param posuX
	 *            the posuX to set
	 */
	public void setPosuX(final int posuX) {
		this.posuX = posuX;
	}

	/**
	 * @param posuY
	 *            the posuY to set
	 */
	public void setPosuY(final int posuY) {
		this.posuY = posuY;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PopiskySettings [font=" + font + ", foreground=" + foreground + ", background=" + background + ", posuX=" + posuX + ", posuY=" + posuY + ", patterns=" + patterns + "]";
	}

}
