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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet1Login;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.forge.*;

public class mod_HyperSigns extends NetworkMod implements IConnectionHandler, IPacketHandler {

	/**
	 * The mod's version number.
	 */
	private static final String VERSION = "1.0";

	/**
	 * Channel name to use for Packet 250 messaging. The '1' in the name is the
	 * protocol version.
	 */
	private static final String CHANNEL_NAME = "HyperSigns1";

	private FMLClientHandler fmlClient;

	@Override
	public String getVersion() {
		return VERSION;
	}

	@Override
	public void load() {
		if (!MinecraftForge.isClient()) {
			MinecraftForge.killMinecraft(this.toString(),
					"One does not simply put the HyperSigns Client on a server!");
		}

		fmlClient = FMLClientHandler.instance();

		MinecraftForge.registerConnectionHandler(this);
	}

	@Override
	public boolean clientSideRequired() {
		return false;
	}

	@Override
	public boolean serverSideRequired() {
		return false;
	}

	/**
	 * Handle incoming data on the plugin messaging channel.
	 * 
	 * @param network
	 *            The NetworkManager for the current connection.
	 * @param channel
	 *            The Channel the message came on.
	 * @param data
	 *            The message payload.
	 */
	@Override
	public void onPacketData(NetworkManager network, String channel, byte[] data) {
		if (!channel.equals(CHANNEL_NAME) || data.length < 1) {
			return;
		}

		ByteArrayInputStream message = new ByteArrayInputStream(data);
		int command = message.read();

		switch (command) {
		// TODO: Put server commands into some sort of enum
		case 0x01:
			handleUrlTrigger(message);
		default:
			return;
		}
	}

	/**
	 * Handle the receipt of a URL Trigger command. The client will be prompted
	 * to open the link received in the packet.
	 * 
	 * @param message
	 *            The input stream containing packet payload.
	 */
	private void handleUrlTrigger(ByteArrayInputStream message) {
		byte[] urlBytes = new byte[message.available()];

		try {
			message.read(urlBytes);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		String urlString = new String(urlBytes);
		URI uri;

		try {
			URL url = new URL(urlString);
			uri = url.toURI();
		} catch (Exception e) {
			// Gotta catch 'em all!
			return;
		}

		fmlClient.displayGuiScreen(fmlClient.getClient().thePlayer, new GuiRemoteLinkConfirm(uri));
	}

	@Override
	public void onConnect(NetworkManager network) {
	}

	@Override
	public void onLogin(NetworkManager network, Packet1Login login) {
		MessageManager.getInstance().registerChannel(network, this, CHANNEL_NAME);
	}

	@Override
	public void onDisconnect(NetworkManager network, String message, Object[] args) {
	}

}
