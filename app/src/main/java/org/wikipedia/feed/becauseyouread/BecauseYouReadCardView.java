package org.wikipedia.feed.becauseyouread;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.view.View;

import org.wikipedia.R;
import org.wikipedia.feed.view.CardHeaderView;
import org.wikipedia.feed.view.CardLargeHeaderView;
import org.wikipedia.feed.view.ListCardItemView;
import org.wikipedia.feed.view.ListCardRecyclerAdapter;
import org.wikipedia.feed.view.ListCardView;
import org.wikipedia.history.HistoryEntry;
import org.wikipedia.views.DefaultViewHolder;
import org.wikipedia.views.ItemTouchHelperSwipeAdapter;

import java.util.List;

public class BecauseYouReadCardView extends ListCardView<BecauseYouReadCard>
        implements ItemTouchHelperSwipeAdapter.SwipeableView {

    public BecauseYouReadCardView(Context context) {
        super(context);
    }

    @Override public void setCard(@NonNull BecauseYouReadCard card) {
        super.setCard(card);
        header(card);
        set(new RecyclerAdapter(card.items()));
    }

    private void header(@NonNull final BecauseYouReadCard card) {
        int age = (int) card.daysOld();
        String subtitle = getSubtitle(age);
        CardHeaderView header = new CardHeaderView(getContext())
                .setTitle(card.title())
                .setSubtitle(subtitle)
                .setImage(R.drawable.ic_restore_black_24dp)
                .setImageCircleColor(R.color.gray_highlight)
                .setCard(card)
                .setCallback(getCallback());
        header(header);
        CardLargeHeaderView largeHeader = new CardLargeHeaderView(getContext())
                .setTitle(card.pageTitle())
                .setImage(card.image())
                .onClickListener(new SelectPageCallbackAdapter(card));
        largeHeader(largeHeader);
    }

    @VisibleForTesting @NonNull String getSubtitle(int age) {
        if (age == 0) {
            return getResources().getString(R.string.view_continue_reading_card_subtitle_today);
        }
        return getResources().getQuantityString(R.plurals.view_continue_reading_card_subtitle, age, age);
    }

    private class SelectPageCallbackAdapter implements OnClickListener {
        @NonNull private final BecauseYouReadCard card;

        SelectPageCallbackAdapter(@NonNull BecauseYouReadCard card) {
            this.card = card;
        }

        @Override public void onClick(View view) {
            if (getCallback() != null) {
                getCallback().onSelectPage(card, new HistoryEntry(card.getPageTitle(),
                        HistoryEntry.SOURCE_FEED_BECAUSE_YOU_READ));
            }
        }
    }

    private class RecyclerAdapter extends ListCardRecyclerAdapter<BecauseYouReadItemCard> {
        RecyclerAdapter(@NonNull List<BecauseYouReadItemCard> items) {
            super(items);
        }

        @Nullable @Override protected ListCardItemView.Callback callback() {
            return getCallback();
        }

        @Override
        public void onBindViewHolder(DefaultViewHolder<ListCardItemView> holder, int i) {
            BecauseYouReadItemCard card = item(i);
            holder.getView().setCard(card)
                    .setHistoryEntry(new HistoryEntry(card.pageTitle(),
                            HistoryEntry.SOURCE_FEED_BECAUSE_YOU_READ));
        }
    }
}
