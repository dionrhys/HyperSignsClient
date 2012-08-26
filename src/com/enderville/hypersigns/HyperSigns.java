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

@Mod(modid = "HyperSigns", name = "HyperSigns", version = "1.1")
@NetworkMod(
		clientSideRequired = false,
		serverSideRequired = false,
		clientPacketHandlerSpec = @SidedPacketHandler(
			channels = {HyperSigns.CHANNEL_NAME},
			packetHandler = PacketHandler.class
		)
	)
public class HyperSigns {

	/**
	 * Channel name to use for Packet 250 messaging. The '1' in the name is the
	 * protocol version.
	 */
	protected static final String CHANNEL_NAME = "HyperSigns1";

	private FMLClientHandler fmlClient;

	@Init
	public void load(FMLInitializationEvent event) {
		if (!FMLCommonHandler.instance().getSide().isClient()) {
			FMLCommonHandler.instance().getFMLLogger().warning("One does not simply put the HyperSigns Client on a server!");
		}
	}

}
