// Copyright Verizon Media. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.

#pragma once

#include <vespa/eval/eval/value_type.h>
#include <vespa/eval/eval/value.h>
#include "streamed_value_index.h"
#include <cassert>

namespace vespalib::eval {

/**
 *  A very simple Value implementation.
 *  Cheap to construct from serialized data,
 *  and cheap to serialize or iterate through.
 *  Slow for full or partial lookups.
 **/
template <typename T>
class StreamedValue : public Value
{
private:
    ValueType _type;
    std::vector<T> _my_cells;
    Array<char> _label_buf;
    StreamedValueIndex _my_index;

public:
    StreamedValue(ValueType type, size_t num_mapped_dimensions,
                  std::vector<T> cells, size_t num_subspaces, Array<char> && label_buf)
      : _type(std::move(type)),
        _my_cells(std::move(cells)),
        _label_buf(std::move(label_buf)),
        _my_index(num_mapped_dimensions,
                  num_subspaces,
                  ConstArrayRef<char>(_label_buf.begin(), _label_buf.size()))
    {
        assert(num_subspaces * _type.dense_subspace_size() == _my_cells.size());
    }

    ~StreamedValue();
    const ValueType &type() const final override { return _type; }
    TypedCells cells() const final override { return TypedCells(_my_cells); }
    const Value::Index &index() const final override { return _my_index; }
    MemoryUsage get_memory_usage() const final override;
    auto get_data_reference() const { return _my_index.get_data_reference(); }
};

} // namespace
