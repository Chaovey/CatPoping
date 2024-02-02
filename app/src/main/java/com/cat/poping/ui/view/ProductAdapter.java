package com.cat.poping.ui.view;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.cat.poping.R;
import com.cat.poping.item.product.Product;
import com.cat.poping.ui.UIEffect;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final Context mContext;
    private final List<Product> mProductList;
    public ProductAdapter(Context context, List<Product> productList) {
        mContext = context;
        mProductList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_recycler_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder viewHolder, final int position) {
        Product product = mProductList.get(position);
        viewHolder.mTxtProductDescription.setText(product.getDescription());
        viewHolder.mImageProduct.setImageResource(product.getDrawableId());
        viewHolder.mBtnProduct.setBackgroundResource(product.getButtonId());
        viewHolder.mBtnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(product);
            }
        });
        UIEffect.createPopUpEffect(viewHolder.itemView, position);
        UIEffect.createPopUpEffect(viewHolder.mBtnProduct, position + 2);
        UIEffect.createButtonEffect(viewHolder.mBtnProduct);
    }

    public void showDialog(Product product) {
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mImageProduct;
        private final TextView mTxtProductDescription;
        private final ImageButton mBtnProduct;

        public ProductViewHolder(View view) {
            super(view);
            mTxtProductDescription = view.findViewById(R.id.txt_product);
            mImageProduct = view.findViewById(R.id.image_product);
            mBtnProduct = view.findViewById(R.id.btn_product);
        }
    }
}
