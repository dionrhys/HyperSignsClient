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
import java.net.URI;
import java.net.URL;

import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.Player;

@Mod(modid = "mod_HyperSigns", name = "HyperSigns", version = "1.1")
@NetworkMod(
		clientSideRequired = false,
		serverSideRequired = false,
		clientPacketHandlerSpec = @SidedPacketHandler(
			channels = {mod_HyperSigns.CHANNEL_NAME},
			packetHandler = mod_HyperSigns.class
		)
	)
public class mod_HyperSigns implements IPacketHandler {

	/**
	 * Channel name to use for Packet 250 messaging. The '1' in the name is the
	 * protocol version.
	 */
	protected static final String CHANNEL_NAME = "HyperSigns1";

	private FMLClientHandler fmlClient;

	@Init
	public void load(FMLInitializationEvent event) {
		if (FMLCommonHandler.instance().getSide().isClient()) {
			fmlClient = FMLClientHandler.instance();
		} else {
			FMLCommonHandler.instance().getFMLLogger().warning("One does not simply put the HyperSigns Client on a server!");
		}
	}

	/**
	 * Handle incoming data on the plugin messaging channel.
	 * 
	 * @param network
	 *            NetworkManager for the current connection.
	 * @param packet
	 *            Custom payload received.
	 * @param player
	 *            Player that sent the message (server only).
	 */
	@Override
	public void onPacketData(NetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		if (!packet.channel.equals(CHANNEL_NAME) || packet.length == 0) {
			return;
		}

		ByteArrayInputStream message = new ByteArrayInputStream(packet.data);
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

}
