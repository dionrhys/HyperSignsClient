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
import java.io.DataInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;

import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

	/**
	 * Handle incoming data on the plugin messaging channel.
	 *
	 * @param manager
	 *            NetworkManager for the current connection.
	 * @param packet
	 *            Custom payload received.
	 * @param player
	 *            Player that sent the message (server only).
	 */
	@Override
	public void onPacketData(NetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		if (!packet.channel.equals(HyperSigns.CHANNEL_NAME)) {
			return;
		}

		DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));

		try {
			byte command = data.readByte();

			switch (command) {
				// TODO: Put server commands into some sort of enum
				case 0x01:
					handleUrlTrigger(data);
				default:
					return;
			}
		} catch (IOException e) {
			FMLLog.log(Level.SEVERE, e, "[HyperSigns] Invalid packet received from server.");
		}
	}

	/**
	 * Handle the receipt of a URL Trigger command. The client will be prompted
	 * to open the link received in the packet.
	 *
	 * @param data
	 *            The DataInputStream containing packet payload.
	 * @throws IOException
	 */
	private void handleUrlTrigger(DataInputStream data) throws IOException {
		String urlString = data.readUTF();
		URI uri;

		try {
			URL url = new URL(urlString);
			uri = url.toURI();
		} catch (Exception e) {
			// Gotta catch 'em all!
			return;
		}

		FMLClientHandler fmlClient = FMLClientHandler.instance();
		fmlClient.displayGuiScreen(fmlClient.getClient().thePlayer, new GuiRemoteLinkConfirm(uri));
	}

}
