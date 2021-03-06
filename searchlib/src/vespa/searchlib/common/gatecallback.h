// Copyright 2017 Yahoo Holdings. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
#pragma once

#include "idestructorcallback.h"

namespace vespalib { class Gate; }

namespace search {

class GateCallback : public IDestructorCallback {
public:
    GateCallback(vespalib::Gate & gate) noexcept : _gate(gate) {}
    ~GateCallback() override;
private:
    vespalib::Gate & _gate;
};

class IgnoreCallback : public IDestructorCallback {
public:
    IgnoreCallback() noexcept { }
    ~IgnoreCallback() override = default;
};

template <typename T>
struct KeepAlive : public search::IDestructorCallback {
    explicit KeepAlive(T toKeep) noexcept : _toKeep(std::move(toKeep)) { }
    ~KeepAlive() override = default;
    T _toKeep;
};

} // namespace search
