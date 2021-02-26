package com.example.tapis_project_v0.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.tapis_project_v0.R;
import com.example.tapis_project_v0.databinding.FragmentHomeBinding;
import com.example.tapis_project_v0.model.Role;
import com.example.tapis_project_v0.model.User;
import com.example.tapis_project_v0.model.UserMotif;
import com.example.tapis_project_v0.ui.MainActivity;
import com.example.tapis_project_v0.ui.adapter.UserMotifRecyclerAdapter;
import com.example.tapis_project_v0.ui.listner.UserMotifsClickListener;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment
        implements UserMotifsClickListener {

    FragmentHomeBinding binding;
    private Context mcontext;
    private List<UserMotif> userMotifs;
    private UserMotifRecyclerAdapter adapter;
    private User currentUser;
    private LinearLayout linearLayout;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_courses_stagged, container, false);

        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        mcontext = this.getContext();
        View view = binding.getRoot();

        MainActivity activity = (MainActivity) getActivity();
        userMotifs = activity.getUserMotifs();
        currentUser = activity.getCurrentUser();
        linearLayout = (LinearLayout) view.findViewById(R.id.partToHideFromAdmin);

        if (currentUser.getRole().equals(Role.ADMIN) && linearLayout != null) {
            linearLayout.setVisibility(View.GONE);
        }

        binding.edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    //For this example only use seach option
                    //U can use a other view with activityresult
                    performSearch();
                    Toast.makeText(mcontext,
                            "Edt Searching Click: " + binding.edtSearch.getText().toString().trim(),
                            Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });

        binding.rvUserMotifs.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );
        binding.rvUserMotifs.setClipToPadding(false);
        binding.rvUserMotifs.setHasFixedSize(true);

        adapter = new UserMotifRecyclerAdapter(mcontext, userMotifs, this);

//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.card_margin);
//        binding.rvCourses.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        binding.rvUserMotifs.setAdapter(adapter);
        return view;
    }

    private void performSearch() {
        binding.edtSearch.clearFocus();
        InputMethodManager in = (InputMethodManager) mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(binding.edtSearch.getWindowToken(), 0);
        //...perform search
    }

    @Override
    public void onDashboardCourseClick(UserMotif userMotif, ImageView imageView) {
        Toast.makeText(mcontext, userMotif.getMotif().getLibelle(), Toast.LENGTH_LONG).show();
    }
}