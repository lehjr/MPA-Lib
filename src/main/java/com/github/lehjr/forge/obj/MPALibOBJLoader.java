/*
 * MPA-Lib (Formerly known as Numina)
 * Copyright (c) 2019 MachineMuse, Lehjr
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.lehjr.forge.obj;

import com.github.lehjr.mpalib.basemod.MPALibLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.VanillaResourceType;
import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 * Loader for modified OBJ models.
 * If you don't need an easy way to get the quads for a given part, then use the Forge version instead.
 * To enable your mod call instance.addDomain(modid).
 *
 * Slightly modified version of Forge's loader because You cannot extend an Enum ^
 */
public enum MPALibOBJLoader implements ICustomModelLoader {
    INSTANCE;

    private static final org.apache.logging.log4j.Logger LOGGER = MPALibLogger.logger;
    private IResourceManager manager;
    private final Set<String> enabledDomains = new HashSet<>();
    private final Map<ResourceLocation, MPALibOBJModel> cache = new HashMap<>();
    private final Map<ResourceLocation, Exception> errors = new HashMap<>();

    public void addDomain(String domain) {
        enabledDomains.add(domain.toLowerCase());
        LOGGER.info("MPALibOBJLoader: Domain {} has been added.", domain.toLowerCase());
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.manager = resourceManager;
        cache.clear();
        errors.clear();
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return enabledDomains.contains(modelLocation.getNamespace()) && modelLocation.getPath().endsWith(".obj");
    }

    @Override
    public IUnbakedModel loadModel(ResourceLocation modelLocation) throws Exception {
        ResourceLocation location = new ResourceLocation(modelLocation.getNamespace(), modelLocation.getPath());
        if (!cache.containsKey(location)) {
            IResource resource = null;
            try {
                try {
                    // the resource manager may not be set due to changes in event registering
                    if (manager == null) {
                        manager = Minecraft.getInstance().getResourceManager();
                    }
                    resource = manager.getResource(location);
                } catch (FileNotFoundException e) {
                    if (modelLocation.getPath().startsWith("models/block/"))
                        resource = manager.getResource(new ResourceLocation(location.getNamespace(), "models/item/" + location.getPath().substring("models/block/".length())));
                    else if (modelLocation.getPath().startsWith("models/item/"))
                        resource = manager.getResource(new ResourceLocation(location.getNamespace(), "models/block/" + location.getPath().substring("models/item/".length())));
                    else throw e;
                }
                MPALibOBJModel.Parser parser = new MPALibOBJModel.Parser(resource, manager);
                MPALibOBJModel model = null;
                try {
                    model = parser.parse();
                } catch (Exception e) {
                    errors.put(modelLocation, e);
                } finally {
                    cache.put(modelLocation, model);
                }
            } finally {
                IOUtils.closeQuietly(resource);
            }
        }
        MPALibOBJModel model = cache.get(location);
        if (model == null)
            throw new ModelLoaderRegistry.LoaderException("Error loading model previously: " + location, errors.get(modelLocation));
        return model;
    }

    @Override
    public IResourceType getResourceType() {
        return VanillaResourceType.MODELS;
    }
}