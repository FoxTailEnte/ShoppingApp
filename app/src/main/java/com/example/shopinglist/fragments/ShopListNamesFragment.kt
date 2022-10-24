package com.example.shopinglist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopinglist.activities.MainApp
import com.example.shopinglist.activities.ShopListActivity
import com.example.shopinglist.database.MainViewModel
import com.example.shopinglist.database.ShopListNameAdapter
import com.example.shopinglist.databinding.FragmentShopListNamesBinding
import com.example.shopinglist.dialogs.DeleteDialog
import com.example.shopinglist.dialogs.NewListDialog
import com.example.shopinglist.entities.ShopListNameItem
import com.example.shopinglist.utils.TimeManager


class ShopListNamesFragment : BaseFragment(), ShopListNameAdapter.Listener {
    private lateinit var binding: FragmentShopListNamesBinding
    private lateinit var adapter: ShopListNameAdapter
    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object: NewListDialog.Listener{
            override fun onClick(name: String) {
                val shopListName = ShopListNameItem(
                    null,
                    name,
                TimeManager.getCurrentTime(),
                0,
                0,
                "")
                mainViewModel.insertShopListName(shopListName)
            }

        },"")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopListNamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }

    private fun initRcView() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = ShopListNameAdapter(this@ShopListNamesFragment)
        rcView.adapter = adapter
    }

    private fun observer() {
        mainViewModel.allShopListName.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = ShopListNamesFragment()
    }

    override fun deleteItem(id: Int) {
        DeleteDialog.showDialog(context as AppCompatActivity, object : DeleteDialog.Listener{
            override fun onClick() {
                mainViewModel.deleteShopList(id, true)
            }

        })
    }

    override fun editItem(shopListName: ShopListNameItem) {
        NewListDialog.showDialog(activity as AppCompatActivity, object: NewListDialog.Listener{
            override fun onClick(name: String) {

                mainViewModel.updateListName(shopListName.copy(name = name))
            }

        },shopListName.name)
    }

    override fun onClickItem(shopListName: ShopListNameItem) {
        val i = Intent(activity, ShopListActivity::class.java).apply {
            putExtra(ShopListActivity.SHOP_LIST_NAME, shopListName)
        }
        startActivity(i)
    }
}