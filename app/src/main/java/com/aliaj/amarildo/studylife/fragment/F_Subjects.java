package com.aliaj.amarildo.studylife.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aliaj.amarildo.studylife.activity.NewSubject;
import com.aliaj.amarildo.studylife.R;
import com.aliaj.amarildo.studylife.utility.Adapter;
import com.aliaj.amarildo.studylife.utility.DataManagment;
import com.aliaj.amarildo.studylife.object.Subject;

import java.util.ArrayList;

/**
 * Fragment PRINCIPALE (HOME) che mostra le materie presenti(inserite dall'utente)
 */

public class F_Subjects extends Fragment {

    ListView listView;  // listView delle materie
    public static Adapter subjectsAdapter; // adatattore per listView
    public static ArrayList<Subject> arrayListSubjects; // arraylist delle materie
    public static int nSubjects; // numero delle materie presenti

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subjects, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) view.findViewById(R.id.list);

        arrayListSubjects = new ArrayList<>();
        nSubjects = 0;

        if(DataManagment.Load(getContext(), 1)) {
            subjectsAdapter = new Adapter(getActivity(), arrayListSubjects, 1);
            listView.setAdapter(subjectsAdapter);
        } else {
            arrayListSubjects = new ArrayList<>();
            subjectsAdapter = new Adapter(getActivity(), arrayListSubjects, 1);
            listView.setAdapter(subjectsAdapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), com.aliaj.amarildo.studylife.activity.SubjectInfo.class);
                intent.putExtra("POSIZIONE", position);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_subjects, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.aggiungi_verifica:
                startActivity(new Intent(getActivity(), NewSubject.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Metodo che in base ai parametri passati aggiunge un elemento(Materia)
     * al arraylist delle materie
     * @param name nome materia
     * @param color colore materia
     * @param image immagine materia
     */
    public static void AddSubject(String name, int color, int image){
        arrayListSubjects.add(nSubjects, new Subject(name, color, image));
        nSubjects++;

        F_Subjects.subjectsAdapter.notifyDataSetChanged();
    }
}
