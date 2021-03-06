package com.bhex.wallet.mnemonic.adapter;

import androidx.appcompat.widget.AppCompatImageView;

import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.ui.item.ImportItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/22
 * Time: 19:04
 */
public class ImportAdapter extends BaseQuickAdapter<ImportItem, BaseViewHolder> {

    public ImportAdapter(int layoutResId, @Nullable List<ImportItem> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, @Nullable ImportItem importItem) {
        viewHolder.setText(R.id.tv_import_title,importItem.title);

        AppCompatImageView iv_import_ic = viewHolder.getView(R.id.iv_import_ic);

        iv_import_ic.setImageDrawable(getContext().getDrawable(importItem.resId));
    }
}
