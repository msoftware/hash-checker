package com.smlnskgmail.jaman.hashchecker.components.filemanager.manager.listadapter;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.smlnskgmail.jaman.hashchecker.R;
import com.smlnskgmail.jaman.hashchecker.components.filemanager.entities.FileItem;
import com.smlnskgmail.jaman.hashchecker.components.filemanager.manager.support.FileSelectTarget;

class FileDialogHolder extends RecyclerView.ViewHolder {

    private final TextView tvItemFile;

    private final FileSelectTarget fileClickListener;

    FileDialogHolder(@NonNull View itemView, @NonNull FileSelectTarget fileClickListener) {
        super(itemView);
        tvItemFile = itemView.findViewById(R.id.tv_item_file);
        this.fileClickListener = fileClickListener;
    }

    void bind(@NonNull FileItem file) {
        loadItem(file);
        itemView.setOnClickListener(v -> fileClickListener.onFileSelect(file, getAdapterPosition()));
    }

    private void loadItem(@NonNull FileItem file) {
        tvItemFile.setText(file.getFileName());
        Drawable fileIcon = ContextCompat.getDrawable(itemView.getContext(), file.getFileType()
                .getIconResId());
        tvItemFile.setCompoundDrawablesRelativeWithIntrinsicBounds(fileIcon, null, null, null);
    }

}
