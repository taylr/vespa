// Copyright 2017 Yahoo Holdings. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
package com.yahoo.config.model.api;

import com.google.common.collect.ImmutableMap;
import com.yahoo.config.provision.ApplicationId;
import com.yahoo.config.provision.TenantName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SuperModel {

    private final Map<ApplicationId, ApplicationInfo> models;

    public SuperModel() {
        this.models = Collections.emptyMap();
    }

    // TODO: Remove when oldest model used is 6.309 or newer
    public SuperModel(Map<TenantName, Map<ApplicationId, ApplicationInfo>> models) {
        this.models = new LinkedHashMap<>();
        models.values().forEach(entry -> entry.forEach(this.models::put));
    }

    // TODO: The ignored parameter is just to circumvent the fact that you cannot have
    // two constructors with same type erasure, the above will be removed and this one can
    // be fixed
    public SuperModel(Map<ApplicationId, ApplicationInfo> models, boolean ignored) {
        this.models = models;
    }

    /**
     * Do NOT mutate the returned map.
     * TODO: Make the returned map immutable (and type to Map&lt;ApplicationId, ApplicationInfo&gt;)
     */
    public Map<TenantName, Map<ApplicationId, ApplicationInfo>> getAllModels() {
        Map<TenantName, Map<ApplicationId, ApplicationInfo>> newModels = new LinkedHashMap<>();

        this.models.forEach((key, value) -> {
            if (!newModels.containsKey(key.tenant())) {
                newModels.put(key.tenant(), new LinkedHashMap<>());
            }
            newModels.get(key.tenant()).put(key, value);
        });
        return newModels ;
    }

    public Map<ApplicationId, ApplicationInfo> getModels() {
        return ImmutableMap.copyOf(models);
    }

    public List<ApplicationInfo> getAllApplicationInfos() {
        return new ArrayList<>(models.values());
    }

    public Optional<ApplicationInfo> getApplicationInfo(ApplicationId applicationId) {
        ApplicationInfo applicationInfo = models.get(applicationId);
        return applicationInfo == null ? Optional.empty() : Optional.of(applicationInfo);
    }

    public SuperModel cloneAndSetApplication(ApplicationInfo application) {
        Map<ApplicationId, ApplicationInfo> newModels = cloneModels(models);
        newModels.put(application.getApplicationId(), application);

        return new SuperModel(newModels, false);
    }

    public SuperModel cloneAndRemoveApplication(ApplicationId applicationId) {
        Map<ApplicationId, ApplicationInfo> newModels = cloneModels(models);
        newModels.remove(applicationId);

        return new SuperModel(newModels, false);
    }

    private static Map<ApplicationId, ApplicationInfo> cloneModels(Map<ApplicationId, ApplicationInfo> models) {
        return new LinkedHashMap<>(models);
    }
}
