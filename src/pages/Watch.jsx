import { useParams, Link } from 'react-router-dom';
import { ThumbsUp, ThumbsDown, Share2, Download, MoreHorizontal } from 'lucide-react';
import { getVideoById, videos } from '../data/videos';
import VideoCard from '../components/VideoCard';

export default function Watch() {
  const { id } = useParams();
  const video = getVideoById(id);
  const related = videos.filter((v) => v.id !== video.id).slice(0, 10);

  return (
    <div className="flex flex-col lg:flex-row gap-6 p-4 md:p-6 max-w-[1800px] mx-auto">
      {/* Main player column */}
      <div className="flex-1 min-w-0">
        {/* Player */}
        <div className="relative aspect-video bg-black rounded-xl overflow-hidden">
          <iframe
            src={`https://www.youtube.com/embed/${video.id}?rel=0&modestbranding=1`}
            title={video.title}
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
            allowFullScreen
            className="absolute inset-0 w-full h-full"
          />
        </div>

        {/* Title & actions */}
        <h1 className="text-xl font-semibold mt-3 leading-snug">{video.title}</h1>

        <div className="flex flex-wrap items-center justify-between gap-3 mt-3">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-full bg-tube-red flex items-center justify-center font-bold">
              {video.channel.charAt(0)}
            </div>
            <div>
              <p className="font-medium text-sm">{video.channel}</p>
              <p className="text-xs text-tube-muted">1.2M subscribers</p>
            </div>
            <button className="ml-2 px-4 py-2 bg-white text-black text-sm font-medium rounded-full hover:bg-gray-200 transition">
              Subscribe
            </button>
          </div>

          <div className="flex items-center gap-2">
            <div className="flex bg-tube-card rounded-full overflow-hidden">
              <button className="flex items-center gap-2 px-4 py-2 hover:bg-tube-hover transition text-sm">
                <ThumbsUp size={18} /> 2.1M
              </button>
              <div className="w-px bg-white/20" />
              <button className="px-4 py-2 hover:bg-tube-hover transition">
                <ThumbsDown size={18} />
              </button>
            </div>
            <button className="flex items-center gap-2 px-4 py-2 bg-tube-card rounded-full hover:bg-tube-hover transition text-sm">
              <Share2 size={18} /> Share
            </button>
            <button className="flex items-center gap-2 px-4 py-2 bg-tube-card rounded-full hover:bg-tube-hover transition text-sm">
              <Download size={18} /> Download
            </button>
            <button className="p-2 bg-tube-card rounded-full hover:bg-tube-hover transition">
              <MoreHorizontal size={18} />
            </button>
          </div>
        </div>

        {/* Description */}
        <div className="mt-4 p-3 bg-tube-card rounded-xl text-sm">
          <p className="font-medium">
            {video.views} views · {video.uploaded}
          </p>
          <p className="mt-2 whitespace-pre-line text-tube-muted leading-relaxed">
            {video.description}
          </p>
        </div>

        {/* Comments placeholder */}
        <div className="mt-6">
          <h3 className="font-medium mb-4">Comments · 12K</h3>
          <div className="flex gap-3">
            <div className="w-10 h-10 rounded-full bg-tube-red flex items-center justify-center font-bold shrink-0">
              S
            </div>
            <input
              type="text"
              placeholder="Add a comment..."
              className="flex-1 bg-transparent border-b border-white/20 py-2 outline-none focus:border-white transition text-sm"
            />
          </div>
        </div>
      </div>

      {/* Related videos */}
      <div className="w-full lg:w-96 shrink-0 space-y-3">
        {related.map((v) => (
          <Link
            key={v.id}
            to={`/watch/${v.id}`}
            className="flex gap-2 group"
          >
            <div className="relative w-40 aspect-video rounded-lg overflow-hidden bg-tube-card shrink-0">
              <img
                src={v.thumbnail}
                alt={v.title}
                className="w-full h-full object-cover"
                loading="lazy"
              />
              <span className="absolute bottom-1 right-1 bg-black/80 text-[10px] px-1 rounded">
                {v.duration}
              </span>
            </div>
            <div className="min-w-0">
              <h4 className="text-sm font-medium line-clamp-2 leading-snug group-hover:text-white">
                {v.title}
              </h4>
              <p className="text-xs text-tube-muted mt-1">{v.channel}</p>
              <p className="text-xs text-tube-muted">
                {v.views} · {v.uploaded}
              </p>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
}