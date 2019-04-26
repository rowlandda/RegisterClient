package edu.uark.uarkregisterapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import edu.uark.uarkregisterapp.R;
import edu.uark.uarkregisterapp.models.api.Product;

public class ProductListAdapter extends ArrayAdapter<Product> {
	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(this.getContext());
			view = inflater.inflate(R.layout.list_view_item_product, parent, false);
		}

		Product product = this.getItem(position);
		if (product != null) {
			TextView lookupCodeTextView = (TextView) view.findViewById(R.id.list_view_item_product_lookup_code);
			if (lookupCodeTextView != null) {
				lookupCodeTextView.setText(product.getLookupCode());
			}

			TextView countTextView = (TextView) view.findViewById(R.id.list_view_item_product_count);
			if (countTextView != null) {
				countTextView.setText(String.format(Locale.getDefault(), "%d", product.getCount()));
			}
			DecimalFormat format = new DecimalFormat("#.00");
			TextView costTextView = (TextView) view.findViewById(R.id.list_view_item_product_cost);
			if (costTextView != null) {
				costTextView.setText(String.format(Locale.getDefault(), "$" +format.format(product.getCost()), product.getCost()));
			}
		}

		return view;
	}

	public ProductListAdapter(Context context, List<Product> products) {
		super(context, R.layout.list_view_item_product, products);
	}
}
