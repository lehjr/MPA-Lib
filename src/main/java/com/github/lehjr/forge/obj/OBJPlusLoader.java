/*
 * Minecraft Forge
 * Copyright (c) 2016-2019.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
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
public enum OBJPlusLoader implements ICustomModelLoader {
    INSTANCE;

    private static final org.apache.logging.log4j.Logger LOGGER = MPALibLogger.logger;
    private IResourceManager manager;
    private final Set<String> enabledDomains = new HashSet<>();
    private final Map<ResourceLocation, OBJPlusModel> cache = new HashMap<>();
    private final Map<ResourceLocation, Exception> errors = new HashMap<>();

    public void addDomain(String domain) {
        enabledDomains.add(domain.toLowerCase());
        LOGGER.info("MuseOBJLoader: Domain {} has been added.", domain.toLowerCase());
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
                OBJPlusModel.Parser parser = new OBJPlusModel.Parser(resource, manager);
                OBJPlusModel model = null;
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
        OBJPlusModel model = cache.get(location);
        if (model == null)
            throw new ModelLoaderRegistry.LoaderException("Error loading model previously: " + location, errors.get(modelLocation));
        return model;
    }

    @Override
    public IResourceType getResourceType() {
        return null;
    }
}