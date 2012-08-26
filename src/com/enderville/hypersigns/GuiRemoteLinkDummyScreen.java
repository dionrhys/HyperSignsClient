/**
 * HyperSignsClient - Client-side plugin for extended in-game sign interaction.
 * Copyright (C) 2012, EnderVille.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.enderville.hypersigns;

import java.net.URI;

import cpw.mods.fml.client.FMLClientHandler;

import net.minecraft.src.GuiScreen;

public class GuiRemoteLinkDummyScreen extends GuiScreen {

	private URI clickedURI = null;

	public GuiRemoteLinkDummyScreen(URI uri) {
		this.clickedURI = uri;
	}

	/**
	 * Callback to handle which button was clicked on the confirmation screen.
	 * 
	 * @param clickedYes
	 *            was the Yes button clicked?
	 * @param par2
	 *            unused
	 */
	@Override
	public void confirmClicked(boolean clickedYes, int par2) {
		if (par2 == 0) {
			if (clickedYes) {
				try {
					Class clazzDesktop = Class.forName("java.awt.Desktop");
					Object methodGetDesktop = clazzDesktop.getMethod("getDesktop", new Class[0])
							.invoke((Object) null, new Object[0]);
					clazzDesktop.getMethod("browse", new Class[] { URI.class }).invoke(
							methodGetDesktop, new Object[] { this.clickedURI });
				} catch (Throwable table) {
					table.printStackTrace();
				}
			}

			this.clickedURI = null;

			FMLClientHandler.instance().getClient().thePlayer.closeScreen();
		}
	}

}
