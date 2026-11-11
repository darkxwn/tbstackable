package com.darkxwn.tbstackable.util;

import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;

/** Дополнительный интерфейс, который mixin добавляет в ItemStackHandler мода: связь хранилища с рюкзаком. */
public interface StackLimitHolder {
    void tbstackable$setWrapper(BackpackWrapper wrapper);
}
