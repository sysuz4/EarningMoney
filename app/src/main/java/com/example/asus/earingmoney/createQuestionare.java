package com.example.asus.earingmoney;

import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.example.asus.earingmoney.Util.Constants;
import com.example.asus.earingmoney.adapter.MyAdapter;
import com.example.asus.earingmoney.lib.Fab;
import com.example.asus.earingmoney.lib.FinishQuestionareDialog;
import com.example.asus.earingmoney.lib.MultiChooseDialog;
import com.example.asus.earingmoney.lib.QueryDialog;
import com.example.asus.earingmoney.lib.SingleChooseDialog;
import com.example.asus.earingmoney.model.QuestionModel;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.FadeEnter.FadeEnter;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.animation.FallEnter.FallEnter;
import com.flyco.animation.FallEnter.FallRotateEnter;
import com.flyco.animation.FlipExit.FlipHorizontalExit;
import com.flyco.animation.NewsPaperEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.animation.ZoomEnter.ZoomInEnter;
import com.flyco.animation.ZoomExit.ZoomOutExit;
import com.flyco.dialog.widget.NormalDialog;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.melnykov.fab.FloatingActionButton;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import java.util.ArrayList;
import java.util.List;


public class createQuestionare extends AppCompatActivity implements AdapterView.OnItemLongClickListener,
        AdapterView.OnItemClickListener, AbsListView.OnScrollListener,
        SlideAndDragListView.OnDragDropListener, SlideAndDragListView.OnSlideListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener,
        SlideAndDragListView.OnItemScrollBackListener{
    private QuestionModel mDraggedEntity;
    private List<QuestionModel> mQueList = new ArrayList<>();
    private Menu mMenu;
    private SlideAndDragListView mListView;
    private MyAdapter myAdapter;
    private MaterialSheetFab materialSheetFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_questionare);

        initData();
        initMenu();
        initUiAndListener();

    }

    private void initUiAndListener() {
        myAdapter = new MyAdapter(mQueList, createQuestionare.this);
        mListView = (SlideAndDragListView) findViewById(R.id.lv_edit);
        mListView.setMenu(mMenu);
        mListView.setAdapter(myAdapter);
        mListView.setOnScrollListener(this);
        mListView.setOnDragDropListener(this);
        mListView.setOnItemClickListener(this);
        mListView.setOnSlideListener(this);
        mListView.setOnMenuItemClickListener(this);
        mListView.setOnItemDeleteListener(this);
        mListView.setOnItemLongClickListener(this);
        mListView.setOnItemScrollBackListener(this);

        Fab fab = (Fab) findViewById(R.id.fab);
        fab.attachToListView(mListView);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.cardview_light_background);
        int fabColor = getResources().getColor(R.color.blue_normal);

        // Initialize material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);
    }

    private void initMenu() {
        mMenu = new Menu(true);

        mMenu.addItem(new MenuItem.Builder().setWidth(120)
                .setBackground(new ColorDrawable(Color.GRAY))//设置菜单的背景
                .setTextColor(Color.BLACK)
                .setTextSize((14))
                .setIcon(getResources().getDrawable(R.mipmap.modify))
                .build());
        mMenu.addItem(new MenuItem.Builder().setWidth(180)
                .setBackground(new ColorDrawable(Color.BLACK))//设置菜单的背景
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setIcon(getResources().getDrawable(R.mipmap.gabege))
                .build());
    }

    private void initData() {
        for(int i = 0; i < 20; i++)
        {
            QuestionModel questionModel = new QuestionModel(2, "hi");
            mQueList.add(questionModel);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return true;
    }

    @Override
    public void onDragViewStart(int beginPosition) {
        mDraggedEntity = mQueList.get(beginPosition);
    }

    @Override
    public void onDragDropViewMoved(int fromPosition, int toPosition) {
        QuestionModel questionModel = mQueList.remove(fromPosition);
        mQueList.add(toPosition, questionModel);
    }

    @Override
    public void onDragViewDown(int finalPosition) {
        mQueList.set(finalPosition, mDraggedEntity);
    }

    @Override
    public void onItemDeleteAnimationFinished(View view, int position) {
        mQueList.remove(position - mListView.getHeaderViewsCount());
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onScrollBackAnimationFinished(View view, int position) {

    }

    @Override
    public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
        switch (direction) {
            case MenuItem.DIRECTION_LEFT:
                switch (buttonPosition) {
                    case 0:
                        modifyQuestion(itemPosition);
                        return Menu.ITEM_SCROLL_BACK;
                }
                break;
            case MenuItem.DIRECTION_RIGHT:
                switch (buttonPosition) {
                    case 0:
                        return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                }
        }
        return Menu.ITEM_NOTHING;
    }

    @Override
    public void onSlideOpen(View view, View parentView, int position, int direction) {

    }

    @Override
    public void onSlideClose(View view, View parentView, int position, int direction) {

    }

    public void createMultiChooseBtn_click(View view) {
        final MultiChooseDialog dialog = new MultiChooseDialog(createQuestionare.this, mQueList);
        dialog.showAnim(new FallRotateEnter())//
                .dismissAnim(new FlipHorizontalExit())//
                .show();
        dialog.setCanceledOnTouchOutside(false);
    }

    public void createSingleChooseBtn_click(View view) {
        final SingleChooseDialog dialog = new SingleChooseDialog(createQuestionare.this, mQueList);
        dialog.showAnim(new ZoomInEnter())//
                .dismissAnim(new ZoomOutExit())//
                .show();
        dialog.setCanceledOnTouchOutside(false);
    }

    public void createQueryBtn_click(View view) {
        final QueryDialog dialog = new QueryDialog(createQuestionare.this, mQueList);
        dialog.showAnim(new BounceTopEnter())//
                .dismissAnim(new SlideBottomExit())//
                .show();
        dialog.setCanceledOnTouchOutside(false);
    }

    public void modifyQuestion(int position)
    {
        QuestionModel questionModel = mQueList.get(position);
        int type = questionModel.getQuestionType();

        if(type == Constants.QUERY_QUESTION)
        {
            final QueryDialog dialog = new QueryDialog(createQuestionare.this, mQueList);
            dialog.setContent(position, myAdapter);
            dialog.showAnim(new BounceTopEnter())//
                    .dismissAnim(new SlideBottomExit())//
                    .show();
            dialog.setCanceledOnTouchOutside(false);
        }
        else if(type == Constants.SINGLE_CHOOSE_QUESTION)
        {
            final SingleChooseDialog dialog = new SingleChooseDialog(createQuestionare.this, mQueList);
            dialog.setContent(position, myAdapter);
            dialog.showAnim(new ZoomInEnter())//
                    .dismissAnim(new ZoomOutExit())//
                    .show();
            dialog.setCanceledOnTouchOutside(false);
        }
        else if(type == Constants.MULTI_CHOOSE_QUESTION)
        {
            final MultiChooseDialog dialog = new MultiChooseDialog(createQuestionare.this, mQueList);
            dialog.setContent(position, myAdapter);
            dialog.showAnim(new FallRotateEnter())//
                    .dismissAnim(new FlipHorizontalExit())//
                    .show();
            dialog.setCanceledOnTouchOutside(false);
        }


    }

    public void finishBtn_click(View view) {
        final FinishQuestionareDialog dialog = new FinishQuestionareDialog(createQuestionare.this);
        dialog.showAnim(new NewsPaperEnter())//
                .dismissAnim(new FadeExit())//
                .show();
        dialog.setCanceledOnTouchOutside(false);
    }
}
