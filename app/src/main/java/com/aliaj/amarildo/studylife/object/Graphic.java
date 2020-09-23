package com.aliaj.amarildo.studylife.object;

import android.content.Context;
import android.view.animation.LinearInterpolator;

import com.aliaj.amarildo.studylife.R;
import com.aliaj.amarildo.studylife.utility.Variables;
import com.db.chart.Tools;
import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.view.LineChartView;

/**
 * Classe che crea il grafico che mostra l'andamento della singola materia
 */

public class Graphic extends LineChartView {

    LineSet lineSet;
    public Animation animation;

    /**
     * Costruttore che comanda l'intera Classe e setta in modo
     * completo il grafico l'asciando alla classe creata solo il
     * metodo per stamparla
     *
     * @param context contesto dell'applicazione da dove viene creata
     * @param subject materia sulla quale si sta creando il grafico
     * @param lineChartView linechart che ospiterà tutti i punti,
     *                      nonchè base del grafico
     */
    public Graphic(Context context, Subject subject, LineChartView lineChartView){
        super(context);
        this.lineSet = new LineSet();

        SetGraphic(lineChartView);
        InsertVotes(subject);
        ModifyLineSet();
        Shade();
        animation = Animation(subject);

        lineChartView.addData(lineSet);
    }

    /**
     * Setta la "grafica" del grafico
     */
    void SetGraphic (LineChartView lineChartView){
        lineChartView.setLabelsColor(getResources().getColor(R.color.Rosso400));
        lineChartView.setClickablePointRadius(3f)
                .setAxisBorderValues(0, 10)
                .setXLabels(AxisRenderer.LabelPosition.NONE);
    }

    /**
     * Cea Point da inserire nel grafico in base ai voti
     * della materia passata come parametro
     * @param subject materia di cui si vogliono "graficare" i voti
     */
    void InsertVotes(Subject subject){
        Point point;
        for (int i = 0; i < subject.getVotes().size(); i++) {
            int month1 = subject.getVotes().get(i).getDate().getMonth();
            String day = String.valueOf(subject.getVotes().get(i).getDate().getDate());
            String month = Variables.MONTHS[month1];

            point = new Point(day + " " + month, subject.getVotes().get(i).getVote());
            lineSet.addPoint(point);
        }
    }

    /**
     * Modifica la grafica della linea del grafico
     */
    void ModifyLineSet(){
        lineSet.setSmooth(false)
                .setDashed(new float[]{8f, 8f})
                .setThickness(Tools.fromDpToPx(2.0f))
                .setDotsRadius(Tools.fromDpToPx(4.0f))
                .setColor(getResources().getColor(R.color.Nero20))
                .setDotsColor(getResources().getColor(R.color.Rosso400));
    }

    /**
     * Imposta la sfumatura sottesa al grafico. L'intervallo di sfumatura va da 0 a 1
     */
    void Shade(){
        int colors[] = new int[2];
        colors[0] = getResources().getColor(R.color.Rosso400);
        colors[1] = getResources().getColor(R.color.Nero20);
        float shadeInterval[] = new float[2];
        shadeInterval[0] = 0.0f;
        shadeInterval[1] = 0.5f;

        lineSet.setGradientFill(colors, shadeInterval);
    }

    /**
     * Imposta l'animazione di apertura dell'activity. I punti vengono alzati in ordine
     * @param subject materia di cui si crea il grafico
     * @return ritorna l'animazione pronta da essere passata alla
     * funzione show
     */
    Animation Animation(Subject subject){
        Animation animation = new Animation();
        animation.setDuration(1500);
        animation.setEasing(new LinearInterpolator());

        int values[] = new int[subject.getVotes().size()];

        for (int i = 0; i < values.length; i++)
            values[i] = i;

        animation.setOverlap(0.5f, values);
        return animation;
    }

    /**
     * Metodo che stampa a video il grafico
     * @param animation animazione con cui inizia il grafico
     * @param lineChartView grafico da mostrare
     */
    public void Show(Animation animation, LineChartView lineChartView){
        lineChartView.show(animation);
    }
}