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

import net.minecraft.src.GuiConfirmOpenLink;
import net.minecraft.src.GuiScreen;

public class GuiRemoteLinkConfirm extends GuiConfirmOpenLink {

	final URI uri;

	public GuiRemoteLinkConfirm(URI uri) {
		super(new GuiRemoteLinkDummyScreen(uri), uri.toString(), 0);
		this.uri = uri;
	}

	@Override
	public void func_50052_d() {
		setClipboardString(uri.toString());
	}

}
