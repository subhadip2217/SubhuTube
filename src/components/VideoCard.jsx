import { Link } from 'react-router-dom';

export default function VideoCard({ video }) {
  return (
    <Link to={`/watch/${video.id}`} className="group block">
      <div className="relative aspect-video rounded-xl overflow-hidden bg-tube-card">
        <img
          src={video.thumbnail}
          alt={video.title}
          className="w-full h-full object-cover group-hover:scale-105 transition duration-300"
          loading="lazy"
        />
        <span className="absolute bottom-1.5 right-1.5 bg-black/80 text-xs px-1.5 py-0.5 rounded font-medium">
          {video.duration}
        </span>
      </div>
      <div className="mt-3 flex gap-3">
        <div className="w-9 h-9 rounded-full bg-tube-red flex items-center justify-center text-sm font-bold shrink-0">
          {video.channel.charAt(0)}
        </div>
        <div className="min-w-0">
          <h3 className="text-sm font-medium line-clamp-2 leading-snug group-hover:text-white">
            {video.title}
          </h3>
          <p className="text-xs text-tube-muted mt-1">{video.channel}</p>
          <p className="text-xs text-tube-muted">
            {video.views} views · {video.uploaded}
          </p>
        </div>
      </div>
    </Link>
  );
}