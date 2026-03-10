# 📊 Adapters Implementation Guide

## ✅ Đã tạo 3 RecyclerView Adapters

### 1️⃣ CategoryAdapter
**File:** `view/adapter/CategoryAdapter.java`

**Chức năng:**
- Hiển thị danh sách categories theo chiều ngang
- Support click listener
- Bind icon và tên category

**Usage:**
```java
CategoryAdapter categoryAdapter = new CategoryAdapter(context);
recyclerView.setLayoutManager(
    new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
);
recyclerView.setAdapter(categoryAdapter);

// Set data
categoryAdapter.setCategories(categories);

// Set click listener
categoryAdapter.setOnCategoryClickListener(category -> {
    // Handle category click
    Toast.makeText(context, category.getName(), Toast.LENGTH_SHORT).show();
});
```

**Layout:** `res/layout/item_category.xml`

---

### 2️⃣ ProductAdapter
**File:** `view/adapter/ProductAdapter.java`

**Chức năng:**
- Hiển thị danh sách products dạng Grid (2 cột)
- Support click listener
- Hiển thị tên, giá sản phẩm
- Placeholder image (có thể tích hợp Glide sau)

**Usage:**
```java
ProductAdapter productAdapter = new ProductAdapter(context);
recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
recyclerView.setAdapter(productAdapter);

// Set data
productAdapter.setProducts(products);

// Set click listener
productAdapter.setOnProductClickListener(product -> {
    // Navigate to product detail
    Intent intent = new Intent(context, ProductDetailActivity.class);
    intent.putExtra("product_id", product.getId());
    context.startActivity(intent);
});
```

**Layout:** `res/layout/item_product.xml`

---

### 3️⃣ OrderAdapter
**File:** `view/adapter/OrderAdapter.java`

**Chức năng:**
- Hiển thị danh sách orders
- Format date và price
- Show status với màu sắc tương ứng
- Support click listener

**Usage:**
```java
OrderAdapter orderAdapter = new OrderAdapter(context);
recyclerView.setLayoutManager(new LinearLayoutManager(context));
recyclerView.setAdapter(orderAdapter);

// Set data
orderAdapter.setOrders(orders);

// Set click listener
orderAdapter.setOnOrderClickListener(order -> {
    // Navigate to order detail
    Intent intent = new Intent(context, OrderDetailActivity.class);
    intent.putExtra("order_id", order.getId());
    context.startActivity(intent);
});
```

**Layout:** `res/layout/item_order.xml`

**Status Colors:**
- Pending (Chờ xác nhận): Orange
- Processing (Đang xử lý): Blue
- Shipped (Đang giao): Purple
- Delivered (Đã giao): Green
- Cancelled (Đã hủy): Red

---

## 📱 Activities đã tích hợp Adapters

### MainActivity
**File:** `MainActivity.java`

**Đã implement:**
```java
✅ CategoryAdapter - Horizontal list categories
✅ ProductAdapter - Grid 2 columns recommended products
✅ Loading state with ProgressBar
✅ Error handling with Toast
✅ MVP pattern integration
✅ Click handlers for categories and products
```

**Data Flow:**
```
1. onCreate() → setupPresenter()
2. presenter.loadCategories()
3. API call → Repository → Presenter
4. Presenter → View.showCategories()
5. CategoryAdapter.setCategories()
6. UI updates
```

### ExploreActivity
**File:** `ExploreActivity.java`

**Đã implement:**
```java
✅ CategoryAdapter - Filter categories
✅ ProductAdapter - All products grid
✅ MVP pattern integration
✅ Loading states
✅ Error handling
✅ Category filter functionality
```

### OrdersActivity
**File:** `OrdersActivity.java`

**Đã implement:**
```java
✅ OrderAdapter - List all orders
✅ MVP pattern integration
✅ Empty state handling
✅ Loading states
✅ Error handling
✅ Order click navigation
```

---

## 🎨 Layout Files

### item_category.xml
```xml
- LinearLayout vertical
- MaterialCardView 72x72dp với rounded corners
- ImageView cho icon
- TextView cho tên category
- Padding và margin appropriate
```

### item_product.xml
```xml
- MaterialCardView với elevation
- FrameLayout cho overlays
- ImageView 180dp height cho product image
- Product title (max 2 lines)
- Rating với star icon
- Price với primary color
- Hot deal và discount badges (optional)
- Favorite button
```

### item_order.xml (New)
```xml
- MaterialCardView với padding
- Order ID và Status badge
- Created date
- Divider line
- Total price highlight
```

---

## 🔄 Data Binding Flow

### Categories
```
API Response → List<Category>
    ↓
CategoryRepositoryImpl
    ↓
MainPresenter.showCategories()
    ↓
MainActivity.showCategories()
    ↓
CategoryAdapter.setCategories()
    ↓
RecyclerView updates
```

### Products
```
API Response → List<Product>
    ↓
ProductRepositoryImpl
    ↓
MainPresenter.showRecommendedProducts()
    ↓
MainActivity.showRecommendedProducts()
    ↓
ProductAdapter.setProducts()
    ↓
RecyclerView updates
```

### Orders
```
API Response → List<Order>
    ↓
OrderRepositoryImpl
    ↓
OrdersPresenter.showOrders()
    ↓
OrdersActivity.showOrders()
    ↓
OrderAdapter.setOrders()
    ↓
RecyclerView updates
```

---

## 🎯 Features Implemented

### ✅ Category Display
- [x] Horizontal scrolling list
- [x] Icon display from resources
- [x] Click to filter products
- [x] Proper spacing and styling

### ✅ Product Display
- [x] Grid layout (2 columns)
- [x] Product name (max 2 lines)
- [x] Formatted price
- [x] Placeholder image
- [x] Click to view details
- [x] Card elevation and corners

### ✅ Order Display
- [x] Linear list layout
- [x] Order ID display
- [x] Formatted date (dd/MM/yyyy HH:mm)
- [x] Status with colors
- [x] Total price formatted
- [x] Click to view details

---

## 🚀 Next Steps

### Phase 1: Image Loading (Priority: High)
```java
// Add Glide dependency
implementation 'com.github.bumptech.glide:glide:4.16.0'

// In ProductAdapter
Glide.with(context)
    .load(product.getImage())
    .placeholder(R.drawable.ic_shopping_bag)
    .error(R.drawable.ic_error)
    .into(imageViewProduct);
```

### Phase 2: Pull-to-Refresh
```xml
<!-- Wrap RecyclerView with SwipeRefreshLayout -->
<androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    android:id="@+id/swipeRefresh"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</androidx.swiperefreshlayout.widget.SwipeRefreshLayout>
```

### Phase 3: Pagination
```java
// Implement EndlessRecyclerViewScrollListener
// Load more items when reaching bottom
```

### Phase 4: Search & Filter
```java
// Add Filterable interface to adapters
// Implement search in real-time
```

---

## 📊 Performance Tips

### ✅ Already Implemented
- ViewHolder pattern
- Efficient data updates with notifyDataSetChanged()
- Proper RecyclerView setup

### 🔄 Can Optimize
```java
// Use DiffUtil for better performance
class ProductDiffCallback extends DiffUtil.Callback {
    // Implement getOldListSize(), getNewListSize()
    // areItemsTheSame(), areContentsTheSame()
}

// Then use:
DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
diffResult.dispatchUpdatesTo(adapter);
```

---

## 🐛 Common Issues & Solutions

### Issue 1: RecyclerView not showing
```
✅ Check: RecyclerView has layout manager
✅ Check: Adapter is set to RecyclerView
✅ Check: Data list is not empty
✅ Check: Layout height is not 0dp without weight
```

### Issue 2: Items not clickable
```
✅ Check: Click listener is set
✅ Check: itemView.setOnClickListener() in ViewHolder
✅ Check: getAdapterPosition() != NO_POSITION
```

### Issue 3: Images not loading
```
✅ Check: Image URLs are valid
✅ Check: Internet permission in manifest
✅ Check: Glide is properly configured
✅ Check: Placeholder is set
```

---

## 📝 Code Examples

### Update Data in Adapter
```java
// Add items
public void addItems(List<Product> newProducts) {
    int startPosition = products.size();
    products.addAll(newProducts);
    notifyItemRangeInserted(startPosition, newProducts.size());
}

// Clear all
public void clear() {
    products.clear();
    notifyDataSetChanged();
}

// Update single item
public void updateItem(int position, Product product) {
    products.set(position, product);
    notifyItemChanged(position);
}
```

### Handle Empty State
```java
if (adapter.getItemCount() == 0) {
    recyclerView.setVisibility(View.GONE);
    emptyStateLayout.setVisibility(View.VISIBLE);
} else {
    recyclerView.setVisibility(View.VISIBLE);
    emptyStateLayout.setVisibility(View.GONE);
}
```

---

## ✅ Checklist

### Before Testing
- [x] All adapters created
- [x] ViewHolders implemented
- [x] Click listeners set up
- [x] Layout managers configured
- [x] Data binding working
- [x] Error handling added

### After Testing
- [ ] Test with real API data
- [ ] Verify click handlers work
- [ ] Check empty states
- [ ] Test loading states
- [ ] Verify proper scrolling
- [ ] Check item spacing

---

**Status:** ✅ Adapters Complete - Ready for Testing

**Date:** 02/02/2026

**Version:** 1.0
